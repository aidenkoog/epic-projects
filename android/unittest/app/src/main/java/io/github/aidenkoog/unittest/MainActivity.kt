package io.github.aidenkoog.unittest

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.github.aidenkoog.unittest.ui.IOnShowLogListener
import io.github.aidenkoog.unittest.ui.TestCase
import io.github.aidenkoog.unittest.ui.TestCase.Companion.setup
import io.github.aidenkoog.unittest.ui.TestInterface
import io.github.aidenkoog.unittest.utils.Constants
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity(), IOnShowLogListener {
    /* ui. */
    private lateinit var leftListView: ListView
    private lateinit var testCaseAdapter: ArrayAdapter<TestCase>
    private lateinit var scrollView: ScrollView
    private lateinit var containerLayout: LinearLayout
    private lateinit var titleTextView: TextView

    /* log date format. */
    private lateinit var timeFormat: SimpleDateFormat

    /* testcases. */
    private var testCaseImpl = TestCaseImpl()
    private var testSequence = 0

    private fun getLastLogView(): View? = containerLayout.getChildAt(containerLayout.childCount - 1)

    @SuppressLint("SimpleDateFormat")
    private fun setTimeFormat() {
        timeFormat = SimpleDateFormat("HH:mm:ss.SSS")
    }

    private fun getFontTypeFace(): Typeface = Typeface.createFromAsset(assets, FONT_PATH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeTestCase()
        setTimeFormat()
        initUiComponent()
        initializeTestCaseAdapter()
        initializeLeftListView()
        loadTestCases()
    }

    @TestInterface(name = Constants.CLEAR_SCREEN, description = "Clean up log messages")
    private fun clearScreen() = clearLogMessages()

    @TestInterface(name = Constants.FINISH_APP, description = "Finish app log messages")
    private fun finishApp() = finishAffinity()

    private fun initUiComponent() {
        leftListView = findViewById(R.id.listView)
        containerLayout = findViewById(R.id.scrollContainer)
        scrollView = findViewById(R.id.scrollView)
        titleTextView = findViewById(R.id.test_app_title)
    }

    private fun loadTestCases() {
        testCaseAdapter.addAll(setup(this))
        testCaseAdapter.addAll(setup(testCaseImpl))
    }

    private fun initializeTestCase() {
        testCaseImpl = TestCaseImpl()
        testCaseImpl.setOnShowLogListener(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.ACTION_DOWN == event.action) {
            if (keyCode == KeyEvent.KEYCODE_1 && event.repeatCount >= 5) {
                clearScreen()
            } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                finishApp()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun executeTestCase(itemIndex: Int) {
        testSequence++
        val testCase = testCaseAdapter.getItem(itemIndex)
        when (testCase?.name) {
            Constants.CLEAR_SCREEN -> if (testSequence > 0) {
                testSequence--
            } else {
                testSequence = 0
            }
        }
        testCase?.let { testCaseRef ->
            testCaseRef.execute({
                if (it != null) {
                    success(it.toString())
                }
            }) {
                failure(it.toString())
            }
        }
    }

    private fun appendTextView(): TextView {
        val view = TextView(this).apply {
            setTextColor(-0x222223)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, LOG_SCREEN_FONT_SIZE)
            isFocusable = false
            setBackgroundResource(android.R.color.transparent)
            typeface = Typeface.createFromAsset(assets, FONT_PATH)
            containerLayout.addView(
                this, LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }
        scrollToBottom()
        return view
    }

    private fun getLastTextView(): TextView {
        val view: View? = getLastLogView()
        return if (view is TextView) {
            view
        } else appendTextView()
    }

    private fun onLog(level: Int, msg: String) {
        runOnUiThread {
            val textView = getLastTextView()
            if (level < 0) {
                textView.append(msg + "\n")
                scrollToBottom()
                return@runOnUiThread
            }
            if (level == Log.ASSERT && msg.isEmpty()) {
                containerLayout.removeAllViews()
                scrollToBottom()
                return@runOnUiThread
            }
            var span: Any? = null
            val lv: String
            when (level) {
                Log.VERBOSE -> lv = "<V>"
                Log.DEBUG -> lv = "<D>"
                Log.INFO -> {
                    lv = "<I>"
                    span = ForegroundColorSpan(Color.CYAN)
                }

                Log.WARN -> {
                    lv = "<SUCCESS>"
                    span = ForegroundColorSpan(Color.GREEN)
                }

                Log.ERROR -> {
                    lv = "<FAILURE>"
                    span = ForegroundColorSpan(Color.RED)
                }

                Log.ASSERT -> lv = "<!>"
                else -> lv = "<?>"
            }
            val time = "[" + timeFormat.format(Date()) + "] "
            val sb = SpannableStringBuilder("$time$lv $msg\n")
            sb.setSpan(
                ForegroundColorSpan(Color.YELLOW),
                0,
                time.length - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (span != null) {
                sb.setSpan(span, time.length, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            textView.append(sb)
            scrollToBottom()
        }
    }

    private fun scrollToBottom() = scrollView.post {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        if (scrollView.hasFocus()) {
            leftListView.requestFocus()
        }
    }

    private fun clearLogMessages() {
        log(Log.ASSERT, "")
        scrollToBottom()
    }

    private fun log(level: Int, log: String?) {
        Log.println(level, TAG, log ?: "")
        onLog(level, log ?: "log null")
    }

    private fun info(log: String?) = log(Log.INFO, log)

    private fun debug(log: String?) = log(Log.DEBUG, log)

    private fun success(log: String?) = log(Log.WARN, log)

    private fun failure(log: String?) = log(Log.ERROR, log)

    private fun assertLog(log: String?) = log(Log.ASSERT, log)

    override fun onLogMessage(logType: Int, message: String?) {
        when (logType) {
            Log.INFO -> info(message)
            Log.DEBUG -> debug(message)
            Log.ASSERT -> assertLog(message)
            Log.ERROR -> failure(message)
            Log.WARN -> success(message)
        }
    }

    private fun initializeTestCaseAdapter() {
        testCaseAdapter =
            object : ArrayAdapter<TestCase>(this, android.R.layout.simple_list_item_2) {
                @SuppressLint("InflateParams")
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    var view = convertView
                    if (view == null) {
                        view = LayoutInflater.from(this@MainActivity)
                            .inflate(android.R.layout.simple_list_item_2, null)
                    }
                    view!!.minimumWidth = LIST_ITEM_MINIMUM_WIDTH
                    view.minimumHeight = LIST_ITEM_MINIMUM_HEIGHT

                    val testCase = getItem(position)
                    val text1 = view.findViewById<TextView>(android.R.id.text1)
                    val text2 = view.findViewById<TextView>(android.R.id.text2)

                    text1.typeface = getFontTypeFace()
                    text1.textSize = LIST_ITEM_TITLE_FONT_SIZE

                    text2.typeface = getFontTypeFace()
                    text2.textSize = LIST_ITEM_DESCRIPTION_FONT_SIZE

                    text1.text = testCase?.name
                    text2.text = testCase?.description
                    return view
                }
            }
    }

    private fun initializeLeftListView() {
        leftListView.adapter = testCaseAdapter
        leftListView.onItemClickListener =
            OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, _: Long ->
                executeTestCase(i)
            }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val LIST_ITEM_MINIMUM_WIDTH = 50
        private const val LIST_ITEM_MINIMUM_HEIGHT = 25
        private const val LOG_SCREEN_FONT_SIZE = 13f
        private const val LIST_ITEM_TITLE_FONT_SIZE = 15f
        private const val LIST_ITEM_DESCRIPTION_FONT_SIZE = 10f
        private const val FONT_PATH = "fonts/DejaVuSansMono.ttf"
    }
}
