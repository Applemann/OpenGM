package com.opengm.view

import javafx.scene.control.TreeItem
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import tornadofx.*


class MainView: View() {

    val drawerView = drawer {
        minWidth = 200.0

        item("Images", expanded = true) {
            listview(listOf("Ball", "Wall", "Paddle").observable())
        }
        item("Sounds") {
            listview(listOf("Music", "Boing").observable())
        }
        item("Background") {
            listview(listOf("Background").observable())
        }
        item("Functions") {
            listview(listOf("RestartGame", "GameOver", "NextLevel", "Pause").observable())
        }
        item("Objects") {
            listview(listOf("Ball", "Wall", "Paddle", "Controller").observable())
        }
        item("Rooms") {
            listview(listOf("Intro", "Level1", "Level2", "Level3", "GameOver").observable())
        }
    }

    val treeView = treeview<String> {
        // Create root item
        root = TreeItem()
        root.isExpanded = true

        check(true)
        // Make sure the text in each TreeItem is the name of the Person
        cellFormat { text = it }

        // Generate items. Children of the root item will contain departments
        populate { parent ->
            if (parent == root) {
                listOf(
                        "Images",
                        "Sounds",
                        "Background",
                        "Functions",
                        "Objects",
                        "Rooms"
                )
            }
            else null
        }



        setOnMouseClicked { event ->
            openInternalWindow<MyFragment>(modal = false)
            println("test")
        }

    }

    override val root = borderpane {
        setMinSize(800.0, 600.0)
        top = hbox {
            imageview {
                image = Image("firewater.jpg")
                fitWidth=24.0
                fitHeight=24.0

                setOnMouseClicked { event ->
                    find<MyFragment>().openWindow(stageStyle = StageStyle.UTILITY)
                }
            }
            button { text = "Sound"; }
            button { text = "Background" }
            button { text = "Functions" }
            button { text = "Objects" }
            button { text = "Rooms" }
        }

        bottom = label("BOTTOM") {
            useMaxWidth = true
            style {
                backgroundColor += Color.BLUE
            }
        }

        left = drawerView



        right = label("RIGHT") {
            useMaxWidth = true
            useMaxHeight = true
            style {
                backgroundColor += Color.PURPLE
            }
        }

        center = hbox {  }
    }
}


class MyFragment: Fragment() {
    override val root = label("This is a popup")
}
