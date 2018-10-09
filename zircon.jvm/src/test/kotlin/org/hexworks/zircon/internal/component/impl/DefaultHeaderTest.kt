package org.hexworks.zircon.internal.component.impl

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.zircon.api.builder.component.ComponentStyleSetBuilder
import org.hexworks.zircon.api.builder.component.HeaderBuilder
import org.hexworks.zircon.api.builder.graphics.StyleSetBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.data.ComponentState
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource
import org.hexworks.zircon.api.resource.ColorThemeResource
import org.hexworks.zircon.api.resource.TilesetResource
import org.junit.Before
import org.junit.Test

class DefaultHeaderTest {

    lateinit var target: DefaultHeader
    lateinit var tileset: TilesetResource

    @Before
    fun setUp() {
        tileset = FONT
        target = HeaderBuilder.newBuilder()
                .withComponentStyleSet(COMPONENT_STYLES)
                .withPosition(POSITION)
                .withTileset(tileset)
                .withText(TEXT)
                .build() as DefaultHeader
    }

    @Test
    fun shouldProperlyReturnText() {
        assertThat(target.text).isEqualTo(TEXT)
    }

    @Test
    fun shouldUseProperFont() {
        assertThat(target.currentTileset().id)
                .isEqualTo(tileset.id)
    }

    @Test
    fun shouldProperlyApplyTheme() {
        target.applyColorTheme(THEME)
        val styles = target.componentStyleSet
        assertThat(styles.fetchStyleFor(ComponentState.DEFAULT))
                .isEqualTo(DEFAULT_STYLE)
        assertThat(styles.fetchStyleFor(ComponentState.MOUSE_OVER))
                .isEqualTo(DEFAULT_STYLE)
        assertThat(styles.fetchStyleFor(ComponentState.FOCUSED))
                .isEqualTo(DEFAULT_STYLE)
        assertThat(styles.fetchStyleFor(ComponentState.ACTIVE))
                .isEqualTo(DEFAULT_STYLE)
        assertThat(styles.fetchStyleFor(ComponentState.DISABLED))
                .isEqualTo(DEFAULT_STYLE)
    }

    @Test
    fun shouldNotAcceptFocus() {
        assertThat(target.acceptsFocus()).isFalse()
    }

    @Test
    fun shouldNotAcceptGivenFocus() {
        assertThat(target.giveFocus()).isFalse()
    }

    companion object {
        val THEME = ColorThemeResource.ADRIFT_IN_DREAMS.getTheme()
        val TEXT = "Button text"
        val FONT = BuiltInCP437TilesetResource.WANDERLUST_16X16
        val POSITION = Position.create(4, 5)
        val DEFAULT_STYLE = StyleSetBuilder.newBuilder()
                .withForegroundColor(THEME.primaryForegroundColor)
                .withBackgroundColor(TileColor.transparent())
                .build()
        val COMPONENT_STYLES = ComponentStyleSetBuilder.newBuilder()
                .withDefaultStyle(DEFAULT_STYLE)
                .build()
    }
}
