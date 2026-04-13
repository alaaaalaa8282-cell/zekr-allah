package com.mohamedabdelazeim.zekr

data class Zekr(
    val id: Int,
    val name: String,
    val audioRes: Int?  // null = text only (no audio yet)
)

object ZekrData {
    val list = listOf(
        Zekr(1, "نُذَكِّركم بالصلاة على النبي ﷺ", R.raw.nozaker_salt_ala_habib),
        Zekr(2, "آية الأحزاب", R.raw.ayah_elahzab),
        Zekr(3, "الحمد لله", R.raw.alhamdo_lelah),
        Zekr(4, "اللهم لك الحمد", R.raw.allahom_lk_alhamd),
        Zekr(5, "لا حول ولا قوة إلا بالله", R.raw.lahawla_wlaqowat),
        Zekr(6, "سبحان الله وبحمده", R.raw.sobhanallah_wabehamdeh),
        Zekr(7, "ربي اغفر لي ولوالدي", R.raw.rbna_ighfer_li)
    )
}
