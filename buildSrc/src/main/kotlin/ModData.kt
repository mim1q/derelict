object ModData {
    const val group = "dev.mim1q"
    const val id = "derelict"
    const val version = "2.1.0"
    const val versionType = "release"
    val mcVersions = listOf("1.20.1")
    val commonDependencies = listOf(
        "fabric-api",
        "owo-lib",
        "fabric-language-kotlin",
    )
    val modrinthDependencies = commonDependencies + listOf(
        "terrablender"
    )
    val curseforgeDependencies = commonDependencies + listOf(
        "terrablender-fabric"
    )
}