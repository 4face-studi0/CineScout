package design

sealed class Palette {

    companion object {

        /**
         * @return [Palette] for given [ColorScheme]
         */
        operator fun invoke(colorScheme: ColorScheme): Palette =
            when(colorScheme) {
                ColorScheme.Light -> Light
                ColorScheme.Dark -> Dark
            }
    }

    abstract val primary: Color
    abstract val primaryLight: Color
    abstract val primaryDark: Color

    abstract val secondary: Color
    abstract val secondaryLight: Color
    abstract val secondaryDark: Color

    abstract val tertiary: Color
    abstract val tertiaryLight: Color
    abstract val tertiaryDark: Color

    abstract val background: Color
    abstract val surface: Color

    object Light : Palette() {

        override val primary = Color.CeladonBlue
        override val primaryLight = Color.CeruleanFrost
        override val primaryDark = Color.BlueSapphire

        override val secondary = Color.Bittersweet
        override val secondaryLight = Color.SalmonPink
        override val secondaryDark = Color.OrangeRedCrayola

        override val tertiary = Color.EtonBlue
        override val tertiaryLight = Color.CambridgeBlue
        override val tertiaryDark = Color.ShinyShamrock

        override val background = Color.Cultured
        override val surface = Color.White
    }

    object Dark : Palette() {

        override val primary = Color.CeladonBlue
        override val primaryLight = Color.CeruleanFrost
        override val primaryDark = Color.BlueSapphire

        override val secondary = Color.Bittersweet
        override val secondaryLight = Color.SalmonPink
        override val secondaryDark = Color.OrangeRedCrayola

        override val tertiary = Color.EtonBlue
        override val tertiaryLight = Color.CambridgeBlue
        override val tertiaryDark = Color.ShinyShamrock

        override val background = Color.RichBlack
        override val surface = Color.EerieBlack
    }
}

enum class ColorScheme { Light, Dark }

inline class Color(val hex: Long) {

    companion object {
        val Bittersweet = Color(0xff6b6b)
        val BlueSapphire = Color(0x386480)
        val CeladonBlue = Color(0x457b9d)
        val CambridgeBlue = Color(0xa4ccB8)
        val CeruleanFrost = Color(0x5590b4)
        val Cultured = Color(0xf9f9f9)
        val EerieBlack = Color(0x232323)
        val EtonBlue = Color(0x87bba2)
        val OrangeRedCrayola = Color(0xff5c5c)
        val RichBlack = Color(0x0C0C0C)
        val SalmonPink = Color(0xff9999)
        val ShinyShamrock = Color(0x6fae8f)
        val White = Color(0xffffff)
    }
}