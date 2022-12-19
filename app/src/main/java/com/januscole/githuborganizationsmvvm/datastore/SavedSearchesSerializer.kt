package com.januscole.githuborganizationsmvvm.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.januscole.githuborganizationsmvvm.SavedSearches
import java.io.InputStream
import java.io.OutputStream

object SavedSearchesSerializer : Serializer<SavedSearches> {
    override val defaultValue: SavedSearches = SavedSearches.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): SavedSearches {
        try {
            return SavedSearches.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read Saved Searches from the Proto Datastore.", exception)
        }
    }

    override suspend fun writeTo(t: SavedSearches, output: OutputStream) = t.writeTo(output)
}
