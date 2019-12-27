package com.example.alohaandroid.ui.ae_chat

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.alohaandroid.BuildConfig
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.ae_chat.fixtures.MessagesFixtures
import com.example.alohaandroid.ui.ae_chat.messages.CustomIncomingImageMessageViewHolder
import com.example.alohaandroid.ui.ae_chat.messages.CustomIncomingTextMessageViewHolder
import com.example.alohaandroid.ui.ae_chat.messages.CustomOutcomingImageMessageViewHolder
import com.example.alohaandroid.ui.ae_chat.messages.CustomOutcomingTextMessageViewHolder
import com.example.alohaandroid.ui.ae_chat.model.Message
import com.example.alohaandroid.ui.ae_phone.addcontact.AddContactActivity
import com.example.alohaandroid.ui.widget.UploadAvatarDialog
import com.example.alohaandroid.utils.FileUtils1

import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_chat.*
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.chat2.ChatManager
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.ui.ae_chat.dialogs.PlaybackDialogFragment
import com.example.alohaandroid.ui.ae_chat.models.ImageLoader
import com.example.alohaandroid.ui.record.RecordingService
import com.example.alohaandroid.ui.record.holders.IncomingVoiceMessageViewHolder
import com.example.alohaandroid.ui.record.holders.OutcomingVoiceMessageViewHolder
import com.example.alohaandroid.utils.extension.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.chat_bottom_sheet.*
import kotlinx.android.synthetic.main.include_toolbar_main.toolbar
import kotlinx.android.synthetic.main.toolbar_personal.*
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.DomainBareJid
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.stringprep.XmppStringprepException
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.net.InetAddress
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HostnameVerifier

@SuppressLint("Registered")
class ChatActivity : BaseActivity(),
    MessagesListAdapter1.OnMessageLongClickListener<Message>,
    MessageInput.InputListener,
    MessageInput.AttachmentsListener,
    UploadAvatarDialog.UploadAvatarDialogListener,
    MessagesListAdapter1.SelectionListener,
    MessageHolders1.ContentChecker<Message>,
    MessagesListAdapter1.OnLoadMoreListener,
    EasyPermissions.PermissionCallbacks, View.OnClickListener {

    private var imageUri: Uri? = null
    private var resultUri: Uri? = null
    private val senderId = "0"
    private lateinit var imageLoader: ImageLoader
    private lateinit var dialog: Dialog
    private var messagesAdapter: MessagesListAdapter1<Message>? = null
    private var mConnection: AbstractXMPPConnection? = null
    private var menu: Menu? = null
    private var selectionCount: Int = 0
    private var lastLoadedDate: Date? = null
    private var mRecordButton: FloatingActionButton? = null
    private var mChronometer: Chronometer? = null
    private var mRecordingPrompt: TextView? = null
    private var mRecordPromptCount = 0
    private var mStartRecording = true
    private var folder: File? = null

    private val messageStringFormatter: MessagesListAdapter1.Formatter<Message>
        get() = MessagesListAdapter1.Formatter { message ->
            val createdAt = SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                .format(message.createdAt)

            var text: String? = message.text
            if (text == null) text = "[attachment]"

            String.format(
                Locale.getDefault(), "%s: %s (%s)",
                message.user.name, text, createdAt
            )
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        imageLoader = ImageLoader { imageView, url, payload ->
            Glide.with(this@ChatActivity).load(url).into(imageView)
        }

        setConnection()
        setSupportActionBar(toolbar)
        initAdapter()
        input.setInputListener(this)
        input.setAttachmentsListener(this)

        ivBack.setOnClickListener(this)
        lnCamera.setOnClickListener(this)
        lnRecord.setOnClickListener(this)
    }

    override fun onSubmit(input: CharSequence): Boolean {
        sendMessage(input.toString(), "nam1@localhost")
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            ivBack.id -> {
                finish()
            }
            lnCamera.id -> {
                UploadAvatarDialog(this, this).show()
                expandBottomsheet()
            }
            lnRecord.id -> {
                dialog = Dialog(this)
                dialog.setContentView(R.layout.dialog_record)
                Objects.requireNonNull<Window>(dialog.window).setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                val window = dialog.window
                val size = Point()
                val display = window?.windowManager?.defaultDisplay
                display?.getSize(size)
                window?.setLayout((size.x * 1), WindowManager.LayoutParams.WRAP_CONTENT)
                window?.setGravity(Gravity.CENTER)
                mRecordButton = dialog.findViewById(R.id.btnRecord) as FloatingActionButton
                mRecordingPrompt = dialog.findViewById(R.id.recording_status_text) as TextView
                mChronometer = dialog.findViewById(R.id.chronometer) as Chronometer
                mRecordButton!!.setOnClickListener {
                    onRecord(mStartRecording)
                    mStartRecording = !mStartRecording
                }
                dialog.show()
                expandBottomsheet()
            }

        }
    }

    // Recording Start/Stop
    @SuppressLint("SetTextI18n")
    private fun onRecord(start: Boolean) {

        val intent = Intent(this, RecordingService::class.java)

        if (start) {
            // start recording
            mRecordButton?.setImageResource(R.drawable.ic_media_stop)
            Toast.makeText(this, R.string.toast_recording_start, Toast.LENGTH_SHORT).show()
            folder =
                File(Environment.getExternalStorageDirectory().toString() + "/SoundRecorder")
            if (!folder!!.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder!!.mkdir()
            }


            //start Chronometer
            mChronometer?.setBase(SystemClock.elapsedRealtime())
            mChronometer?.start()
            mChronometer?.onChronometerTickListener = Chronometer.OnChronometerTickListener {
                if (mRecordPromptCount == 0) {
                    mRecordingPrompt?.text = getString(R.string.record_in_progress) + "."
                } else if (mRecordPromptCount == 1) {
                    mRecordingPrompt?.text = getString(R.string.record_in_progress) + ".."
                } else if (mRecordPromptCount == 2) {
                    mRecordingPrompt?.text = getString(R.string.record_in_progress) + "..."
                    mRecordPromptCount = -1
                }

                mRecordPromptCount++
            }

            //start RecordingService
            this.startService(intent)
            //keep screen on while recording
            this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            mRecordingPrompt?.setText(getString(R.string.record_in_progress) + ".")
            mRecordPromptCount++

        } else {
            //stop recording
            mRecordButton?.setImageResource(R.drawable.ic_mic_white_36dp)
            messagesAdapter?.addToStart(sendVoice(), true)
            mChronometer?.stop()
            mChronometer?.base = SystemClock.elapsedRealtime()
            mRecordingPrompt?.setText(getString(R.string.record_prompt))
            dialog.dismiss()
            this.stopService(intent)
            //allow the screen to turn off again once recording is finished
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    private fun sendVoice(): Message {
        val elapsedMillis: Long = SystemClock.elapsedRealtime() - mChronometer!!.base
        val message = Message("0", MessagesFixtures.getUserSent(), null)
        message.voice = Message.Voice(1, folder?.path, elapsedMillis)
        return message
    }

    override fun onAddAttachments() {
        requestPermission()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AddContactActivity.REQUEST_CAMERA -> {
                CropImage.activity(imageUri).start(this)
            }

            AddContactActivity.REQUEST_SELECT_FILE -> {
                data?.let { CropImage.activity(it.data).start(this) }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    resultUri = result.uri
                    encodeImage(resultUri!!.path)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                }

            }

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onGetPhotoGallery() {
        openGallery()
    }

    override fun onTakePhotos() {
        takePicture()
    }

    override fun onMessageLongClick(message: Message) {}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.chat_actions_menu, menu)
        onSelectionChanged(0)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> messagesAdapter!!.deleteSelectedMessages()
            R.id.action_copy -> {
                messagesAdapter!!.copySelectedMessagesText(
                    this,
                    messageStringFormatter,
                    true
                )
                showToast(getString(R.string.copied_message))
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed()
        } else {
            messagesAdapter!!.unselectAllItems()
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages()
        }
    }

    override fun onSelectionChanged(count: Int) {
        this.selectionCount = count
        menu?.findItem(R.id.action_delete)?.isVisible = count > 0
        menu?.findItem(R.id.action_copy)?.isVisible = count > 0
    }

    override fun hasContentFor(message: Message?, type: Byte): Boolean {
        when (type) {
            CONTENT_TYPE_VOICE -> return (message?.getVoice() != null
                    && message.voice.url != null
                    && !message.voice.url.isEmpty())
        }
        return false
    }

    private fun loadMessages() {
        Handler().postDelayed(//imitation of internet connection
            {
                val messages = MessagesFixtures.getMessages(lastLoadedDate)
                lastLoadedDate = messages[messages.size - 1].createdAt
                //  messagesAdapter!!.addToEnd(messages, false)
            }, 100
        )
    }

    private fun sendMessage(messageSend: String, entityBareId: String) {

        var jid: EntityBareJid? = null
        try {
            jid = JidCreate.entityBareFrom(entityBareId)
        } catch (e: XmppStringprepException) {
            e.printStackTrace()
        }

        if (mConnection != null) {

            val chatManager = ChatManager.getInstanceFor(mConnection)
            val chat = chatManager.chatWith(jid)
            val newMessage = org.jivesoftware.smack.packet.Message()
            newMessage.body = messageSend

            try {
                chat.send(newMessage)

                val message2 = Message("0", MessagesFixtures.getUserSent(), messageSend)
                messagesAdapter?.addToStart(message2, true)


            } catch (e: SmackException.NotConnectedException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    private fun setConnection() {

        object : Thread() {
            override fun run() {

                var addr: InetAddress? = null
                try {
                    // inter your ip4address now checking it
                    addr = InetAddress.getByName("192.168.1.55")
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                }

                val verifier = HostnameVerifier { hostname, session -> false }
                var serviceName: DomainBareJid? = null
                try {
                    serviceName = JidCreate.domainBareFrom("192.168.1.55")
                } catch (e: XmppStringprepException) {
                    e.printStackTrace()
                }
                val config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("nam2", "123456")
                    .setPort(5222)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setXmppDomain(serviceName)
                    .setHostnameVerifier(verifier)
                    .setHostAddress(addr)
                    .setDebuggerEnabled(true)
                    .build()
                mConnection = XMPPTCPConnection(config)

                try {
                    (mConnection as XMPPTCPConnection).connect()
                    // all these proceedure also thrown error if you does not seperate this thread now we seperate thread create
                    (mConnection as XMPPTCPConnection).login()

                    if ((mConnection as XMPPTCPConnection).isAuthenticated() && (mConnection as XMPPTCPConnection).isConnected()) {
                        //now send message and receive message code here

                        val chatManager = ChatManager.getInstanceFor(mConnection)
                        chatManager.addListener(IncomingChatMessageListener { from, message, chat ->

                            //now update recyler view
                            runOnUiThread {
                                //this ui thread important otherwise error occur
                                if (message.body.contains("/9j/")) {
                                    Log.d("ll", message.body)
                                    decodeImage(message.body)

                                } else {
                                    val message1 = Message(
                                        "1",
                                        MessagesFixtures.getUserReceive(), message.body
                                    )
                                    messagesAdapter?.addToStart(message1, true)
                                }


                            }
                        })
                    }
                } catch (e: SmackException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: XMPPException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }.start()
    }

    private fun initAdapter() {
        //We can pass any data to ViewHolder with payload
        val payload = CustomIncomingTextMessageViewHolder.Payload()
        //For example click listener
        payload.avatarClickListener =
            CustomIncomingTextMessageViewHolder.OnAvatarClickListener {
            }

        val holdersConfig = MessageHolders1()
            .setIncomingTextConfig(
                CustomIncomingTextMessageViewHolder::class.java,
                R.layout.item_custom_incoming_text_message,
                payload
            )
            .setOutcomingTextConfig(
                CustomOutcomingTextMessageViewHolder::class.java,
                R.layout.item_custom_outcoming_text_message
            )
            .setIncomingImageConfig(
                CustomIncomingImageMessageViewHolder::class.java,
                R.layout.item_custom_incoming_image_message
            )
            .setOutcomingImageConfig(
                CustomOutcomingImageMessageViewHolder::class.java,
                R.layout.item_custom_outcoming_image_message
            )
            .registerContentType(
                CONTENT_TYPE_VOICE,
                IncomingVoiceMessageViewHolder::class.java,
                R.layout.item_custom_incoming_voice_message,
                OutcomingVoiceMessageViewHolder::class.java,
                R.layout.item_custom_outcoming_voice_message,
                this
            )

        messagesAdapter =
            MessagesListAdapter1(senderId, holdersConfig, imageLoader)

        messagesAdapter!!.registerViewClickListener(R.id.ivPlayRecord,
            object : MessagesListAdapter1.OnMessageViewClickListener<Message> {
                override fun onMessageViewClick(view: View?, message: Message?) {
                    try {
                        val playbackFragment = PlaybackDialogFragment().newInstance(message?.voice)

                        val transaction = (this@ChatActivity as AppCompatActivity)
                            .supportFragmentManager
                            .beginTransaction()

                        playbackFragment.show(transaction, "dialog_playback")

                    } catch (e: Exception) {
                    }

                }
            })
        messagesAdapter?.setOnMessageLongClickListener(this)
        messagesAdapter?.setLoadMoreListener(this)
        messagesAdapter?.enableSelectionMode(this)

        messagesList.setAdapter(messagesAdapter)
    }

    private fun requestPermission() {
        if (EasyPermissions.hasPermissions(
                this,
                *AddContactActivity.CAMERA_AND_READ_WRITE_STORAGE
            )
        ) {
            // Have permissions, do the thing
            expandBottomsheet()
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_read_and_write_to_storage),
                AddContactActivity.RC_CAMERA_AND_READ_WRITE_STORAGE,
                *AddContactActivity.CAMERA_AND_READ_WRITE_STORAGE
            )
        }
    }

    private fun takePicture() {
        val photoFile = FileUtils1.createImageFile(this)
        imageUri = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".fileprovider",
            photoFile
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, AddContactActivity.REQUEST_CAMERA)
    }

    private fun openGallery() {
        startActivityForResult(Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }, AddContactActivity.REQUEST_SELECT_FILE)
    }

    private fun sendImage(messageSend: String, entityBareId: String) {

        var jid: EntityBareJid? = null
        try {
            jid = JidCreate.entityBareFrom(entityBareId)
        } catch (e: XmppStringprepException) {
            e.printStackTrace()
        }

        if (mConnection != null) {

            val chatManager = ChatManager.getInstanceFor(mConnection)
            val chat = chatManager.chatWith(jid)
            val newMessage = org.jivesoftware.smack.packet.Message()
            newMessage.body = messageSend
            try {
                chat.send(messageSend)
            } catch (e: SmackException.NotConnectedException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    private fun encodeImage(path: String) {
        val imagefile = File(path)
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(imagefile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bm = BitmapFactory.decodeStream(fis)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        val encImage = Base64.encodeToString(b, Base64.DEFAULT)
        val message2 = Message("0", MessagesFixtures.getUserSent(), null)
        message2.setImage(Message.Image(path))
        messagesAdapter?.addToStart(message2, true)
        sendImage(encImage, "nam1@localhost")

    }

    private fun decodeImage(encodeImage: String) {
        val decodedString = Base64.decode(encodeImage, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        getImageUri(this, decodedByte)

    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "Title",
            null
        )
        val message2 =
            Message("1", MessagesFixtures.getUserReceive(), null)
        message2.setImage(Message.Image(Uri.parse(path).toString()))
        messagesAdapter?.addToStart(message2, true)
        return Uri.parse(path)
    }

    private fun expandBottomsheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollViewMobileContact)

        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    companion object {
        private val TOTAL_MESSAGES_COUNT = 100
        private val CONTENT_TYPE_VOICE: Byte = 1
    }

}



