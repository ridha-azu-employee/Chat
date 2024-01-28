package com.azu.chat.chat_collection.chat_user.request;

public record ChatUserRequest (
         Long userId,

         String email,

         String name,

         String image,

         String mobile
){


}
