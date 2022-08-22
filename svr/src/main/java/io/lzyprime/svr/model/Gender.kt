package io.lzyprime.svr.model

enum class Gender {
    Unknown,
    Male,
    Female,
    Secret;

    internal companion object {
        operator fun invoke(ordinal: Int) = values().getOrNull(ordinal) ?: Unknown
    }
}