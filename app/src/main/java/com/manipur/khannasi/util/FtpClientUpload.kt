package com.manipur.khannasi.util

import android.os.AsyncTask
import org.apache.commons.net.PrintCommandListener
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPConnectionClosedException
import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter


object FtpClientUpload {
    const val SERVER = "82.112.237.3"
    const val PORT = 21
    const val USER = "ftpuser"
    const val PASS = "ftpuser"
    const val FILE_PATH = "/home/ftpuploads/images/"

    @JvmStatic
    fun uploadFile(filePath: String, input: InputStream) {
        val uploadTask = object : UploadTask(SERVER, PORT, USER, PASS, FILE_PATH+filePath, input) {
            override fun onPostExecute(result: Boolean) {
                super.onPostExecute(result)
                // Handle the result of the upload here
                if (result) {
                    println("Upload successful")
                } else {
                    println("Upload failed")
                }
            }
        }
        uploadTask.execute()
    }
}
abstract class UploadTask(
    private val server: String,
    private val port: Int,
    private val user: String,
    private val pass: String,
    private val filePath: String,
    private val input: InputStream
) :
    AsyncTask<Void?, Void?, Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean? {
        val ftp = FTPClient()
        try {
            ftp.connect(server, port)
            if (!ftp.login(user, pass)) {
                ftp.logout()
                return false
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE)
            ftp.enterLocalPassiveMode()
            ftp.addProtocolCommandListener( PrintCommandListener(PrintWriter(System.out), true))
            val result = ftp.storeFile(filePath, input)
            input.close()
            ftp.logout()
            ftp.disconnect()
            return result
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } catch (ex: FTPConnectionClosedException) {
            println("FTP server closed the connection: ${ex.message}")
            ex.printStackTrace()
            return false
        }
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        // Handle the result of the upload here
    }
}