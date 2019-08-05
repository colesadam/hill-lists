package uk.colessoft.android.hilllist.ui.activity.dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerAppCompatActivity
import uk.colessoft.android.hilllist.ui.viewmodel.ViewModelFactory

abstract class BaseActivity : DaggerAppCompatActivity() {


    inline fun <reified T : ViewModel> ViewModelFactory<T>.get(): T =
            ViewModelProviders.of(this@BaseActivity, this)[T::class.java]
}