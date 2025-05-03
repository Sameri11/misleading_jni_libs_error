package com.example.app_test

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import java.io.File

class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get and log the native library path
        val nativeLibraryPath = applicationContext.applicationInfo.nativeLibraryDir
        Log.d("nativeloader", "Native Library Path: $nativeLibraryPath")

        // Print paths to all potential native library locations
        printNativeLibraryPaths(nativeLibraryPath)
    }

    private fun printNativeLibraryPaths(nativeLibraryPath: String) {
        try {
            // Get package info using the appropriate API based on Android version
            val packageInfo = packageManager.let { pm ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
                } else {
                    pm.getPackageInfo(packageName, 0)
                }
            }

            // Get first supported ABI and prepare path suffix for lib directories
            val firstAbi = Build.SUPPORTED_ABIS.firstOrNull() ?: return
            val libPathSuffix = "!${File.separatorChar}lib${File.separatorChar}$firstAbi"

            // Get base APK lib path
            val baseApkPath = packageInfo.applicationInfo?.sourceDir
            val baseApkLibDir = baseApkPath?.plus(libPathSuffix) ?: ""

            // Get split APK lib paths
            val splitSourceDirs = packageInfo.applicationInfo?.splitSourceDirs.orEmpty()
            val splitLibPaths = splitSourceDirs.map { it + libPathSuffix }

            // Log the combined path list
            val allPaths = listOfNotNull(
                nativeLibraryPath,
                baseApkLibDir.takeIf { it.isNotEmpty() }
            ) + splitLibPaths

            // Join and log all paths
            Log.d("nativeLoader", "Load library path: ${allPaths.joinToString(":")}")

            // Optional: Log info about splits for debugging
            if (splitSourceDirs.isNotEmpty()) {
                Log.d("MainActivity", "Found ${splitSourceDirs.size} split APKs")
            } else {
                Log.d("MainActivity", "No split APKs found")
            }

        } catch (e: Exception) {
            Log.e("MainActivity", "Error getting native library paths", e)
        }
    }
}
