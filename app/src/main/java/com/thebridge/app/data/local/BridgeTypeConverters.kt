package com.thebridge.app.data.local

import androidx.room.TypeConverter

class BridgeTypeConverters {
    @TypeConverter
    fun fromUserMode(value: UserMode): String = value.name

    @TypeConverter
    fun toUserMode(value: String): UserMode = UserMode.valueOf(value)

    @TypeConverter
    fun fromEventSource(value: EventSource): String = value.name

    @TypeConverter
    fun toEventSource(value: String): EventSource = EventSource.valueOf(value)

    @TypeConverter
    fun fromConversationActor(value: ConversationActor): String = value.name

    @TypeConverter
    fun toConversationActor(value: String): ConversationActor = ConversationActor.valueOf(value)
}
