package org.hexworks.zircon.benchmark

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.DrawSurfaces

import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.TrueTypeFontResources
import org.hexworks.zircon.api.builder.application.AppConfigBuilder
import org.hexworks.zircon.api.builder.graphics.LayerBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.data.GridPosition
import java.awt.Toolkit
import java.util.*

object SwingScreenBenchmark {

    @JvmStatic
    fun main(args: Array<String>) {

        val dimensions = Toolkit.getDefaultToolkit().screenSize
        val tileset = TrueTypeFontResources.ibmBios(20)
        val size = Size.create(dimensions.width / tileset.width, dimensions.height / tileset.width)

        val screen = Screen.create(SwingApplications.startTileGrid(AppConfigBuilder.newBuilder()
                .withSize(size)
                .withDefaultTileset(tileset)
                .withDebugMode(true)
                .build()))

        screen.display()

        val random = Random()
        val terminalWidth = size.width
        val terminalHeight = size.height
        val layerCount = 20
        val layerWidth = size.width / 2
        val layerHeight = size.height / 2
        val layerSize = Size.create(layerWidth, layerHeight)
        val filler = Tile.defaultTile().withCharacter('x')

        val layers = (0..layerCount).map {

            val imageLayer = DrawSurfaces.tileGraphicsBuilder()
                    .withSize(layerSize)
                    .withTileset(tileset)
                    .build()
            layerSize.fetchPositions().forEach {
                imageLayer.draw(filler, it)
            }

            val layer = LayerBuilder.newBuilder()
                    .withOffset(Position.create(
                            x = random.nextInt(terminalWidth - layerWidth),
                            y = random.nextInt(terminalHeight - layerHeight)))
                    .withTileGraphics(imageLayer)
                    .build()

            screen.addLayer(layer)
            layer
        }

        val chars = listOf('a', 'b')

        var currIdx = 0


        while (true) {
            val tile = Tile.defaultTile().withCharacter(chars[currIdx])
            fillGrid(screen, tile)
            layers.forEach {
                it.asInternalLayer().moveTo(Position.create(
                        x = random.nextInt(terminalWidth - layerWidth),
                        y = random.nextInt(terminalHeight - layerHeight)))
            }
            currIdx = if (currIdx == 0) 1 else 0
        }
    }


    private fun fillGrid(tileGrid: TileGrid, tile: Tile) {
        (0..tileGrid.size.height).forEach { y ->
            (0..tileGrid.size.width).forEach { x ->
                tileGrid.draw(tile, GridPosition(x, y))
            }
        }
    }
}
