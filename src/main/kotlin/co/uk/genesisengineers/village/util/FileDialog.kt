package co.uk.genesisengineers.village.util

import org.lwjgl.PointerBuffer
import org.lwjgl.util.nfd.NativeFileDialog

import org.lwjgl.system.MemoryUtil.memAllocPointer
import org.lwjgl.system.MemoryUtil.memFree
import org.lwjgl.util.nfd.NativeFileDialog.*


class FileDialog(private val requestCode: Int, private val fileDialogInterface: FileDialogInterface) {
    fun showSaveDialog() {
        val outPath = memAllocPointer(1)
        try {
            checkResult(
                    NFD_SaveDialog("png,jpg;pdf", null, outPath),
                    outPath
            )
        } finally {
            memFree(outPath)
        }
    }

    fun showLoadDialog() {
        val outPath = memAllocPointer(1)
        try {
            checkResult(
                    NFD_OpenDialog("png,jpg;pdf", null, outPath),
                    outPath
            )
        } finally {
            memFree(outPath)
        }
    }

    private fun checkResult(result: Int, path: PointerBuffer) {
        when (result) {
            NFD_OKAY -> {
                fileDialogInterface.fileDialogSuccess(requestCode, path.getStringUTF8(0))
                nNFDi_Free(path.get(0))
            }
            NFD_CANCEL -> fileDialogInterface.fileDialogCancel(requestCode)
            else // NFD_ERROR
            -> fileDialogInterface.fileDialogError(requestCode, NativeFileDialog.NFD_GetError())
        }
    }

    interface FileDialogInterface {
        fun fileDialogSuccess(requestCode: Int, filePath: String)
        fun fileDialogError(requestCode: Int, error: String?)
        fun fileDialogCancel(requestCode: Int)
    }
}