package com.mano.churchpodcast.util

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.pm.Signature
import com.google.common.io.BaseEncoding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class ApplicationUtil {

    companion object {

        @JvmStatic
        @Suppress("DEPRECATION")
        fun getSignature(pm: PackageManager, packageName: String): String? {
            return try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P){
                    val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                    if (packageInfo?.signingInfo == null) {
                        return null
                    }
                    return if (packageInfo.signingInfo.hasMultipleSigners()){
                        signaturesDigest(packageInfo.signingInfo.apkContentsSigners)
                    } else{
                        signaturesDigest(packageInfo.signingInfo.signingCertificateHistory)
                    }
                } else{
                    @SuppressLint("PackageManagerGetSignatures")
                    val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                    return signaturesDigest(packageInfo.signatures)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }

        @JvmStatic
        private fun signaturesDigest(sig: Array<Signature>): String? {
            val signature = if (sig.isNotEmpty()) {
                sig[0]
            } else return null
            return try {
                val md = MessageDigest.getInstance("SHA1")
                val digest = md.digest(signature.toByteArray())
                BaseEncoding.base16().lowerCase().encode(digest)
            } catch (e: NoSuchAlgorithmException) {
                null
            }
        }

    }

}