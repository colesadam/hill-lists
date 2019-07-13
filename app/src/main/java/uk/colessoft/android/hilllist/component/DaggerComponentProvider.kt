package uk.colessoft.android.hilllist.component

import android.app.Activity

interface DaggerComponentProvider {

    val dbComponent: DatabaseComponent
}

val Activity.injector get() = (application as DaggerComponentProvider).dbComponent
