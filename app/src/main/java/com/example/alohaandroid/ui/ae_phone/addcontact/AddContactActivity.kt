package com.example.alohaandroid.ui.ae_phone.addcontact

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.alohaandroid.BuildConfig
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.ui.ae_phone.aloha.AlohaContactFragment.Companion.ALOHA_OR_MOBILE_CONTACT
import com.example.alohaandroid.ui.widget.UploadAvatarDialog
import com.example.alohaandroid.utils.FileUtils1
import com.example.alohaandroid.utils.extension.showToast
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.activity_add_contact.edtEmail
import kotlinx.android.synthetic.main.activity_add_contact.edtName
import kotlinx.android.synthetic.main.toolbar_personal.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.util.*
import java.util.regex.Pattern

class AddContactActivity : BaseActivity(), UploadAvatarDialog.UploadAvatarDialogListener,
    View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private lateinit var uploadAvatarViewModel: UploadAvatarViewModel
    private lateinit var createContactViewModel: CreateContactViewModel

    private var imageUri: Uri? = null
    private var resultUri: Uri? = null
    private var idAlohaOrMobile: Int? = null

    var mBitmap: Bitmap? = null
    var thumbnail: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        initCreateContactViewmodel()
        initUploadProfileViewmodel()

        ivBack.setOnClickListener(this)
        tvTitleRight.setOnClickListener(this)
        ivAvatar.setOnClickListener(this)
        ivBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cancel))
        tvTitleLeft.text = getString(R.string.add_contact)
        tvTitleRight.text = getString(R.string.title_save)
        if (intent != null) {
            if (intent.hasExtra(ALOHA_OR_MOBILE_CONTACT)) {
                idAlohaOrMobile = intent.getIntExtra(ALOHA_OR_MOBILE_CONTACT, 0)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            ivBack.id -> {
                finish()
            }
            tvTitleRight.id -> {
                if (isValidEmailId(edtEmail.text.toString())) {
                    createContactViewModel.createContact(
                        edtPhone.text.toString(),
                        edtName.text.toString(),
                        edtEmail.text.toString(),
                        2,
                        0
                    )
                    addInforContact()
                } else {
                    showToast(getString(R.string.error_incorrect_email))
                }


            }
            ivAvatar.id -> {
                requestPermission()
            }
        }
    }

    override fun onGetPhotoGallery() {
        if (idAlohaOrMobile == 2) {
            openGallery()

        } else if (idAlohaOrMobile == 3) {
            pickPhoto()

        }
    }

    override fun onTakePhotos() {
        if (idAlohaOrMobile == 2) {
            takePicture()
        } else if (idAlohaOrMobile == 3) {
            takePhotoFromCamera()
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

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CAMERA -> {
                CropImage.activity(imageUri).start(this)

            }

            REQUEST_SELECT_FILE -> {
                data?.let { CropImage.activity(it.data).start(this) }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    resultUri = result.uri
                    Glide.with(this).load(resultUri.toString()).into(ivAvatar)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                }

            }

            PICK_PHOTO -> {
                // Getting the uri of the picked photo
                val selectedImage = data?.data

                var imageStream: InputStream? = null
                try {
                    // Getting InputStream of the selected image
                    if (selectedImage != null) {

                        imageStream = contentResolver.openInputStream(selectedImage)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }


                // Creating bitmap of the selected image from its inputstream
                mBitmap = BitmapFactory.decodeStream(imageStream)
                if (mBitmap != null) {
                    ivAvatar.setImageBitmap(mBitmap)
                } else {
                    ivAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.iv_call))
                }
            }
            CAMERA -> {
                try {
                    thumbnail = data?.extras?.get("data") as Bitmap
                    ivAvatar?.setImageBitmap(thumbnail)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun isValidEmailId(email: String): Boolean {

        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
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
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun openGallery() {
        startActivityForResult(Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }, REQUEST_SELECT_FILE)
    }

    private fun initUploadProfileViewmodel() {
        uploadAvatarViewModel =
            ViewModelProviders.of(this).get(UploadAvatarViewModel::class.java).apply {
                uploadAvatarSuccess.observe(this@AddContactActivity, Observer {
                    it?.let {
                      

                    }
                })

                uploadAvatarFailr.observe(this@AddContactActivity, Observer {

                })
                loadingStatus.observe(this@AddContactActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }

    }

    private fun initCreateContactViewmodel() {
        createContactViewModel =
            ViewModelProviders.of(this).get(CreateContactViewModel::class.java).apply {
                createContactSuccess.observe(this@AddContactActivity, Observer {
                    it?.let {
                        if (resultUri?.path != null) {
                            uploadAvatarViewModel.uploadUserAvatar(resultUri?.path!!)
                        }

                        val returnIntent = Intent()
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                })

                message.observe(this@AddContactActivity, Observer {
                    it?.let {
                        showToast(it)
                    }
                })

                loadingStatus.observe(this@AddContactActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }

    }

    private fun requestPermission() {
        if (EasyPermissions.hasPermissions(this, *CAMERA_AND_READ_WRITE_STORAGE)) {
            // Have permissions, do the thing!
            UploadAvatarDialog(this, this).show()
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_read_and_write_to_storage),
                RC_CAMERA_AND_READ_WRITE_STORAGE,
                *CAMERA_AND_READ_WRITE_STORAGE
            )
        }
    }

    private fun pickPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO)
    }

    private fun addInforContact() {
        val ops = ArrayList<ContentProviderOperation>()

        val rawContactID = ops.size

        // Adding insert operation to operations list
        // to insert a new raw contact in the table ContactsContract.RawContacts
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                    edtName.getText().toString()
                )
                .build()
        )

        // Adding insert operation to operations list
        // to insert Mobile Number in the table ContactsContract.Data
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    edtPhone.getText().toString()
                )
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                )
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.Email.ADDRESS,
                    edtEmail.getText().toString()
                )

                .build()
        )

        val stream = ByteArrayOutputStream()
        if (mBitmap != null) {    // If an image is selected successfully
            mBitmap!!.compress(Bitmap.CompressFormat.PNG, 75, stream)

            // Adding insert operation to operations list
            // to insert Photo in the table ContactsContract.Data
            ops.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                    .build()
            )

            try {
                stream.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        if (thumbnail != null) {    // If an image is selected successfully
            thumbnail!!.compress(Bitmap.CompressFormat.PNG, 75, stream)

            // Adding insert operation to operations list
            // to insert Photo in the table ContactsContract.Data
            ops.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                    .build()
            )

            try {
                stream.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        try {
            // Executing all the insert operations as a single database transaction
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            finish()
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: OperationApplicationException) {
            e.printStackTrace()
        }


    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    companion object {
         val CAMERA_AND_READ_WRITE_STORAGE = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
         const val RC_CAMERA_AND_READ_WRITE_STORAGE = 4466
         const val REQUEST_CAMERA = 140
         const val PICK_PHOTO = 1
         const val CAMERA = 3
         const val REQUEST_SELECT_FILE = 266
    }
}
