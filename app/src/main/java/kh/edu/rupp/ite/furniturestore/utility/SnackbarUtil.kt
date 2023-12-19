package kh.edu.rupp.ite.furniturestore.utility

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kh.edu.rupp.ite.furniturestore.R

object SnackbarUtil {
    @SuppressLint("ResourceAsColor")
    fun showSnackBar(context: Context, view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

        // If you want to customize the Snackbar, you can use snackbar.getView()
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))


        val params = snackbarView.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = context.resources.getDimensionPixelSize(R.dimen.padding_10)
        params.marginEnd = context.resources.getDimensionPixelSize(R.dimen.padding_10)
        snackbarView.layoutParams = params

        // Show the Snackbar
        snackbar.show()
    }
}
