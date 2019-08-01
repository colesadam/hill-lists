package uk.colessoft.android.hilllist.activity.dialogs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import uk.colessoft.android.hilllist.viewmodel.ViewModelFactory

abstract class BaseActivity : DaggerAppCompatActivity() {


    inline fun <reified T : ViewModel> ViewModelFactory<T>.get(): T =
            ViewModelProviders.of(this@BaseActivity, this)[T::class.java]
}