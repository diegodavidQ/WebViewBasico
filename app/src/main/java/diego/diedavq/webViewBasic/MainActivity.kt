package diego.diedavq.webViewBasic

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.SearchView

class MainActivity : AppCompatActivity() {

    //Private
    private val BASE_URL = "https://google.com"
    private val SEARCH_PATH = "/search?q="    //busqueda en el motor de búsqueda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Refresh
        swipeRefresh.setOnRefreshListener {
            webView.reload()
        }

        //searchView //metodos cuando escribe en el searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextChange(newText: String?): Boolean { //sugire busquedas que se han realizado previamente
                return false   //desactivado
            }

            override fun onQueryTextSubmit(query: String?): Boolean { //se invoca cuando el usuario da click en el icono de buscar
                //acceder al texto escrito
                query?.let { //si el texto es distinto de nulo ingresa a la operación
                    if(URLUtil.isValidUrl(it)){
                        //es una URL
                        webView.loadUrl(it)
                    }else {
                        //no es una URL
                        webView.loadUrl("$BASE_URL$SEARCH_PATH$it") //hacer una busqueda en el motor de google
                    }
                }


                return false //para manejar uno mismo su comportamiento
            }
        })


        //webview
        webView.webChromeClient = object : WebChromeClient() {

        }

        webView.webViewClient = object : WebViewClient() {

            //control sobre las nuevas cargas de URL
            override fun shouldOverrideUrlLoading( view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            //cuando una nueva URL se empieza a cargar
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                searchView.setQuery(url,false) //mostrar en el searchView la URL actual de la webView

                swipeRefresh.isRefreshing = false
            }
        }

        //activar javaScript en el webView
        val settings = webView.settings
        settings.javaScriptEnabled = true

        webView.loadUrl(BASE_URL)

    }



    //boton de retroceso
    override fun onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack()
        }
        else{
            super.onBackPressed()
        }
    }



}
