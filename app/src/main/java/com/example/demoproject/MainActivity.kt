package com.example.demoproject

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoproject.databinding.ActivityMainBinding
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import com.obsez.android.lib.filechooser.ChooserDialog


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var rowsArrayList: ArrayList<String> = ArrayList()
    var isLoading = false
//    lateinit var linearLayoutManager :LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var rowsArrayList = DataResource().loadData()
        println(rowsArrayList)

        binding.rvRecyclerView.adapter = ListAdapter(this, rowsArrayList)
//        linearLayoutManager =LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true)
//        binding.rvRecyclerView.layoutManager = linearLayoutManager
//        binding.rvRecyclerView.setHasFixedSize(true)

        initScrollListener()

        binding.txtOpen.setOnClickListener {
//            chooseFile()
//            openGalleryForImage()
            pickFIle()

        }

    }

    private fun pickFIle() {
        val chooseFile: Intent = Intent(this, FilePickerActivity::class.java)
        chooseFile.putExtra(
            FilePickerActivity.CONFIGS, Configurations.Builder()
                .setCheckPermission(true)
                .setShowFiles(true)
                .setShowImages(false)
                .setShowVideos(false)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .setSuffixes("doc", "docx", "xls", "xlsx", "zip","apk")
                .build()
        )

        startActivityForResult(chooseFile, 100);
    }

    private fun openGalleryForImage() {
        val chooseFile: Intent = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "*/*"
        startActivityForResult(chooseFile, 500)

    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 100 && resultCode === RESULT_OK && attr.data != null) {
            val mediaFiles: List<MediaFile> = data?.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!
            if (mediaFiles != null) {
                val path = FileUtils(this@MainActivity).getPath(mediaFiles[0].uri)
                println("path 1=>>"+path)
                println("path =>>"+mediaFiles[0].uri.path)
                Toast.makeText(this@MainActivity, "path =>>"+mediaFiles[0].uri.path, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this@MainActivity, "Image not selected", Toast.LENGTH_SHORT).show()
            }
        }

//        if (resultCode == Activity.RESULT_OK && requestCode == 500 && data != null) {
//
////            val mediaFiles: List<MediaFile> =
////                data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!
////            val path = mediaFiles[0].path
//
//            val uri: Uri? = data?.data
//////            val FilePath = getRealPathFromURI(uri!!) // should the path be here in this string
//            val path = FileUtils(this@MainActivity).getPath(uri)
////            println("path===>" + path)
//            Toast.makeText(this, "path=>" + path, Toast.LENGTH_SHORT).show() // handle chosen image
////            Toast.makeText(this, "path=>" + path, Toast.LENGTH_SHORT).show() // handle chosen image
//        }
    }


    private fun chooseFile() {

        val chooserDialog: ChooserDialog = ChooserDialog(this)


        chooserDialog
            .withStartFile(Environment.getExternalStorageDirectory().absolutePath)
            // .withStartFile(Environment.getExternalStorageState()+"/
//            .withFilterRegex(false, false, ".*\\.(zip)")
            .withFilter(false, false, "xls", "xlsx", "xlsm", "xlsb", "zip")
//            .withFilter(false, false, "tif", "tiff", "gif", "jpeg", "jpg", "jif", "jfif",
//                "jp2", "jpx", "j2k", "j2c", "fpx", "pcd", "png", "pdf")

            .withChosenListener { path, pathFile ->
                Toast.makeText(this, "FILE: $path / $pathFile", Toast.LENGTH_SHORT).show()
            }
            .build()
            .show()
    }

    private fun initScrollListener() {
        binding.rvRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager?.findLastCompletelyVisibleItemPosition() == (rowsArrayList.size - 1)) {
                        //bottom of list!
                        println("come hear")
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        println("call load more")
    }

}