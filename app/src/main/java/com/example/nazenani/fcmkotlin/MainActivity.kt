package com.example.nazenani.fcmkotlin

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.moshi.Moshi
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import retrofit2.HttpException


// https://segunfamisa.com/posts/using-retrofit-on-android-with-kotlin
// https://android.jlelse.eu/kotlin-and-retrofit-2-tutorial-with-working-codes-333a4422a890
// https://dev.classmethod.jp/smartphone/android-kotlin-introduction-05/
// https://chunksofco.de/getting-json-body-from-retrofit-error-kotlin-6a2281875503
// http://blog.asobicocoro.com/entry/2016/12/19/092121


class MainActivity : AppCompatActivity(), PermissionHelper {
    override val message: String? get() = null;
    override val caption: String? get() = null;
    override val REQUEST_CODE: Int get() = 1;
    override val PERMISSION: String get() = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // FCMトークンを取得
        //getToken();

        // ボタンクリック処理
        button1.setOnClickListener {
            execute(this);

            val job = launch(UI) {
                try {
                    val info = async(CommonPool) {
                        var hoge = MyApiService.create().get("nazenani@example.co.jp", "password");
                        hoge.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<Result> {
                            override fun onSubscribe(d: Disposable) {
                                Log.d("onSubscribe", d.toString());
                            }

                            override fun onComplete() {
                                Log.d("onComplete", it.toString());
                            }

                            override fun onError(e: Throwable) {
                                if (e is HttpException) {
                                    val errorJsonString =e.response().errorBody()?.string();
                                    val moshiAdapter = Moshi.Builder().build().adapter(Object::class.java);
                                    Log.d("onError", moshiAdapter.fromJson(errorJsonString).toString());
                                }
                                Log.d("onError", e.toString());
                            }

                            override fun onNext(t: Result) {
                                Log.d("onNext", t.toString());
                            }
                        })
                    }.await();
                    Log.d("Kotlinコルーチン", "success");
                } catch (e: Exception) {
                    Log.d("Kotlinコルーチン", "failure: " + e);
                }
            }

        }


        button2.setOnClickListener {
        }

    }


    override fun onAllowed() {
        //throw UnsupportedOperationException();
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super<PermissionHelper>.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    /**
     * FCMトークンを取得
     */
    private fun getToken() {
        val token = FirebaseInstanceId.getInstance().token
        if (token != null) {
            Log.d("token", token)
        }
    }

/*
    fun <T> async(context: CoroutineContext = CommonPool, start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> T)
            = kotlinx.coroutines.experimental.async(context, start, block)

    fun ui(start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit)
            = launch(UI, start, block)
*/

}
