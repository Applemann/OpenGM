package com.opengm.view

import javafx.scene.control.TreeItem
import javafx.scene.image.Image
import javafx.stage.StageStyle
import tornadofx.*



class MainView: View() {


    fun loadResources() : MutableMap<String, List<String>> {
        val resources = mutableMapOf<String, List<String>>()
        resources["Images"] = listOf("Ball", "Wall", "Paddle")
        resources["Sounds"] = listOf("Music", "Boing")
        resources["Backgrounds"] = listOf("Background")
        resources["Functions"] = listOf("RestartGame", "GameOver", "NextLevel", "Pause")
        resources["Objects"] = listOf("Ball", "Wall", "Paddle", "Controller")
        resources["Rooms"] = listOf("Intro", "Level1", "Level2", "Level3", "GameOver")
        return resources
    }

    val drawerView = drawer {
        minWidth = 200.0

        val allResources = loadResources()

        for (res in allResources) {
            item(res.key) {

                treeview<String> {
                    // Create root item
                    root = TreeItem(res.key)
                    root.isExpanded = true

                    // Make sure the text in each TreeItem is the name of the Person
                    cellFormat { text = it }

                    // Generate items. Children of the root item will contain departments
                    populate { parent ->
                        if (parent == root) {
                            res.value as List<String>
                        } else null
                    }
                }
            }
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


        val allResources = loadResources()

        //left = drawerView
        left = treeview<String> {
            // Create root item
            root = TreeItem("ArkanoidGame")
            root.isExpanded = true

            // Make sure the text in each TreeItem is the name of the Person
            cellFormat { text = it }

            // Generate items. Children of the root item will contain departments
            populate { parent ->
                if (parent == root) {
                    allResources.keys
                } else {
                    allResources[parent.value]
                }
            }

            setOnMouseClicked { event ->

                //println(event.target)

            }
            setOnMouseDragOver { event ->

            }
            setOnMouseDragEntered {
                println("entered")
            }
            setOnDragOver {

                println("dragiing")
            }
            setOnDragDetected { event ->
                  //println(event.target)
                println("start drag")

//                /* Put a string on a dragboard */
//                val content = ClipboardContent()
//                content.putString(event.source.toString())
//                db.setContent(content)
//
//                event.consume()
            }


        }


    }
}


class MyFragment: Fragment() {
    override val root = label("This is a popup")
}
