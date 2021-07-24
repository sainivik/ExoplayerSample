package com.sainivik.`interface`

import com.sainivik.model.LanguageModel

interface OptionClickListener {
    fun click(pos: Int = -1, data: LanguageModel)

}