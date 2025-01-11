package io.github.aidenkoog.unittest.ui

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class TestCase internal constructor(
    @JvmField val name: String,
    @JvmField val description: String,
    private val runOnBackground: Boolean,
) {
    @Throws(Exception::class)
    protected abstract fun execute(): Any?

    @DelicateCoroutinesApi
    fun execute(onSuccess: OnSuccessListener?, onFailure: OnFailureListener?) {
        if (runOnBackground) {
            GlobalScope.launch {
                var exception: Exception? = null
                launch(Dispatchers.IO) {
                    try {
                        this@TestCase.execute()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        exception = e
                    }
                }.join()
                launch(Dispatchers.Main) {
                    if (exception is Exception) {
                        onFailure?.onFailure(exception)
                    } else {
                        onSuccess?.onSuccess(exception)
                    }
                }
            }
        } else {
            try {
                val result = execute()
                onSuccess?.onSuccess(result)
            } catch (e: Exception) {
                onFailure?.onFailure(e)
            }
        }
    }

    fun interface OnSuccessListener {
        fun onSuccess(data: Any?)
    }

    fun interface OnFailureListener {
        fun onFailure(e: Exception?)
    }

    companion object {
        fun setup(target: Any): List<TestCase> {
            val testCases: MutableList<TestCase> = ArrayList()
            for (method in target.javaClass.declaredMethods) {
                method.isAccessible = true
                val testCase = method.getAnnotation(TestInterface::class.java) ?: continue
                if (method.parameterTypes.isNotEmpty()) {
                    continue
                }
                val name: String = testCase.name.ifEmpty {
                    method.name
                }
                val item: TestCase =
                    object : TestCase(name, testCase.description, testCase.runOnBackground) {
                        @Throws(Exception::class)
                        public override fun execute(): Any? {
                            return method.invoke(target)
                        }
                    }
                testCases.add(item)
            }
            return testCases
        }
    }
}
