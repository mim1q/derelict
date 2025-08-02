package datagen.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import tada.lib.generator.ResourceGenerator
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.exists

class PostProcessFileSaver(
    val postProcessPaths: (path: String) -> String,
    val postProcessContent: (path: String, json: JsonObject) -> JsonObject,
    val postProcessContentString: (path: String, json: String) -> String,
) : ResourceGenerator.FileSaver {
    private val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    override fun prepare(baseDirectory: Path) {}

    override fun save(filePath: Path, content: String) {
        val postProcessedPath = Path.of(postProcessPaths(filePath.toString().replace("\\", "/")))
        val jsonContent = JsonParser.parseString(content).asJsonObject
        val postProcessedContent = postProcessContent(postProcessedPath.toString(), jsonContent)
        val postProcessedJson =
            postProcessContentString(postProcessedPath.toString(), gson.toJson(postProcessedContent))

        Files.createDirectories(postProcessedPath.parent)
        if (!postProcessedPath.exists()) {
            postProcessedPath.createFile()
        }
        postProcessedPath.toFile().writeText(postProcessedJson)
    }
}