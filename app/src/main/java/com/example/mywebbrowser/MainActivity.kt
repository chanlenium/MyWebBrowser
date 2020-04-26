package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS
import org.jetbrains.anko.share

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setting to operate the Javascript
        webView.apply{
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient() // To use webview
        }
        webView.loadUrl("https://www.google.com")   // load basic page

        // Define the operation of search button
        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {  // IME_ACTION_SEARCH: constant value corresponding to search button defined in "EditorInfo" class
                webView.loadUrl(urlEditText.text.toString())
                true
            } else{
                false
            }
        }

        // Register context menu
        unregisterForContextMenu(webView)   // Indicate webView as the view displaying context menu
    }

    // Redefine onBack func
    override fun onBackPressed() {
        if(webView.canGoBack()){    // If there is previous page..
            webView.goBack()
        }else{    // If there is not previous page..
            super.onBackPressed()   // Ending activity
        }
    }

    // Override option menu registration method
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflater: xml파일을 Java 코드에 접근하여 실제 객체로 만들어서 사용하는 것
        menuInflater.inflate(R.menu.main, menu) // Register menu Inflater (display menu resource as activity menu)
        return true //if true is return, program thinks there is menu in activity
    }

    // Override option menu click operation method
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_google, R.id.action_home ->{
                webView.loadUrl("https://www.google.com")
                return true
            }
            R.id.action_naver -> {
                webView.loadUrl("https://m.naver.com")
                return true
            }
            R.id.action_daum -> {
                webView.loadUrl("https://m.daum.net")
                return true
            }
            R.id.action_call -> {   // open call app
                /*
                암시적 인텐트 : 의도가 불 명확한 인텐트, 암시적 인텐트에는 클래스명이나 패키지명을 넣지 않는다.
                예) 내가 전화번호를 가지고 있어, 내가 원하는 액션은 ACTION_DIAL인데 이거를 처리해줄 엑티비티는 누구니?
                -> 디바이스에 설치된 앱 중 해당 액션과 Uri를 처리할  수 있는 액티비티를 찾아 실행해준다.
                암시적 인텐트는 내부적으로 Activity Manager가 PackageMansger에 해당 인텐트와 Uri를 처리할 가장 적합한 액티비티가 어떤것인지 물어본다 (resolveActivity)
                EX) if (intent.resolveActivity(getPackageManager()) != null) { startActivity(intent); }
                 */
                val intent = Intent(Intent.ACTION_DIAL) // Declare intent
                intent.data = Uri.parse("tel:010-5355-5286")    // dedicate data to intent
                if(intent.resolveActivity(packageManager) != null){ // Check whether the activity operating this intent(i.e., call app)
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                // sendSMS(Number, [Contents])
                sendSMS("010-5355-5286", webView.url)
                return true
            }
            R.id.action_email -> {
                // email(email address, [Title], [Contents])
                email("dynamic.oh@gmail.com", "Ask for your webBrowser", webView.url)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // onCreateContextMenu: 특정 객체를 장시간 누를 경우 발생하는 이벤트를 담당
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu) // display context resource as activity menu
    }

    // Override context menu click operation method
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_share -> {
                share(webView.url)
                return true
            }
            R.id.action_browser -> {
                browse(webView.url)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
