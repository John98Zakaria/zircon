package org.hexworks.zircon.api.builder.component

import org.hexworks.zircon.api.component.CheckBox
import org.hexworks.zircon.api.component.builder.base.BaseComponentBuilder
import org.hexworks.zircon.api.component.data.ComponentMetadata
import org.hexworks.zircon.api.component.renderer.ComponentRenderer
import org.hexworks.zircon.internal.component.impl.DefaultCheckBox
import org.hexworks.zircon.internal.component.renderer.DefaultCheckBoxRenderer
import org.hexworks.zircon.internal.component.renderer.DefaultComponentRenderingStrategy
import kotlin.jvm.JvmStatic
import kotlin.math.max

@Suppress("UNCHECKED_CAST")
class CheckBoxBuilder(
        private var text: String = "")
    : BaseComponentBuilder<CheckBox, CheckBoxBuilder>(DefaultCheckBoxRenderer()) {

    fun withText(text: String) = also {
        this.text = text
        contentSize = contentSize
                .withWidth(max(text.length + DefaultCheckBoxRenderer.DECORATION_WIDTH, contentSize.width))
    }

    override fun build(): CheckBox {
        return DefaultCheckBox(
                componentMetadata = ComponentMetadata(
                        size = size,
                        relativePosition = position,
                        componentStyleSet = componentStyleSet,
                        tileset = tileset),
                initialText = text,
                renderingStrategy = DefaultComponentRenderingStrategy(
                        decorationRenderers = decorationRenderers,
                        componentRenderer = componentRenderer as ComponentRenderer<CheckBox>)).apply {
            colorTheme.map {
                theme = it
            }
        }
    }

    override fun createCopy() = newBuilder().withProps(props.copy())
            .withText(text)

    companion object {

        @JvmStatic
        fun newBuilder() = CheckBoxBuilder()
    }
}
