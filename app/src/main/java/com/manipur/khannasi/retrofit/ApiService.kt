package com.manipur.khannasi.retrofit

import com.manipur.khannasi.dto.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.DELETE

interface ApiService {
    @Multipart
    @POST("api/upload-image")
    fun uploadImage(@Part image: MultipartBody.Part): Call<Map<String, Any>>

    // UserDetailsController
    @GET("api/user-details")
    fun getAllUserDetails(): Call<List<UserDetails>>

    @GET("api/user-details/{userId}")
    fun getUserDetailsById(@Path("userId") userId: Long): Call<UserDetails?>

    @GET("api/user-details/{username}/{password}")
    fun getUserDetailsByUsernameAndPassword(@Path("username") username: String, @Path("password") password: String): Call<UserDetails?>

    @POST("api/user-details")
    fun postUserDetails(@Body userDetails: UserDetails): Call<UserDetails>

    @PUT("api/user-details/{userId}")
    fun putUserDetails(@Path("userId") userId: Long, @Body userDetails: UserDetails): Call<UserDetails?>

    @DELETE("api/user-details/{userId}")
    fun deleteUserDetails(@Path("userId") userId: Long): Call<Void>

    // UserBasicsController
    @GET("api/user-basics")
    fun getAllUserBasics(): Call<List<UserBasics>>

    @GET("api/user-basics/{id}")
    fun getUserBasicsById(@Path("id") id: Long): Call<UserBasics>

    @GET("api/user-basics/{username}")
    fun getUserBasicsByUsername(@Path("username") username: String): Call<UserBasics>

    @POST("api/user-basics")
    fun postUserBasics(@Body userBasics: UserBasics): Call<UserBasics>

    @PUT("api/user-basics/{id}")
    fun putUserBasics(@Path("id") id: Long, @Body userBasics: UserBasics): Call<UserBasics>

    @DELETE("api/user-basics/{id}")
    fun deleteUserBasics(@Path("id") id: Long): Call<Void>

    // ArticleCommentController
    @GET("api/article-comments")
    fun getAllArticleComments(): Call<List<ArticleComment>>

    @GET("api/article-comments/{id}")
    fun getArticleCommentById(@Path("id") id: Long): Call<ArticleComment>

    @POST("api/article-comments")
    fun postArticleComment(@Body comment: ArticleComment): Call<ArticleComment>

    @PUT("api/article-comments/{id}")
    fun putArticleComment(@Path("id") id: Long, @Body comment: ArticleComment): Call<ArticleComment>

    @DELETE("api/article-comments/{id}")
    fun deleteArticleComment(@Path("id") id: Long): Call<Void>

    @GET("api/article-comments/article/{articleId}")
    fun getCommentsByArticleId(@Path("articleId") articleId: Long): Call<List<ArticleComment>>


    // ArticleController
    @GET("api/articles")
    fun getAllArticles(): Call<List<Article>>

    @GET("api/articles/{id}")
    fun getArticleById(@Path("id") id: Long): Call<Article>

    @POST("api/articles")
    fun postArticle(@Body article: Article): Call<Article>

    @PUT("api/articles/{id}")
    fun putArticle(@Path("id") id: Long, @Body article: Article): Call<Article>

    @DELETE("api/articles/{id}")
    fun deleteArticle(@Path("id") id: Long): Call<Void>

    // ArticleVoteController
    @GET("api/votes")
    fun getAllArticleVotes(): Call<List<ArticleVote>>

    @GET("api/votes/{id}")
    fun getArticleVoteById(@Path("id") id: Long): Call<ArticleVote>

    @POST("api/votes")
    fun postArticleVote(@Body vote: ArticleVote): Call<ArticleVote>

    @PUT("api/votes/{id}")
    fun putArticleVote(@Path("id") id: Long, @Body vote: ArticleVote): Call<ArticleVote>

    @DELETE("api/votes/{id}")
    fun deleteArticleVote(@Path("id") id: Long): Call<Void>

    // DiscussionCommentController
    @GET("api/discussion-comments")
    fun getAllDiscussionComments(): Call<List<DiscussionComment>>

    @GET("api/discussion-comments/{id}")
    fun getDiscussionCommentById(@Path("id") id: Long): Call<DiscussionComment>

    @POST("api/discussion-comments")
    fun postDiscussionComment(@Body comment: DiscussionComment): Call<DiscussionComment>

    @PUT("api/discussion-comments/{id}")
    fun putDiscussionComment(@Path("id") id: Long, @Body comment: DiscussionComment): Call<DiscussionComment>

    @DELETE("api/discussion-comments/{id}")
    fun deleteDiscussionComment(@Path("id") id: Long): Call<Void>

    @GET("api/discussion-comments/discussion/{discussionId}")
    fun getCommentsByDiscussionId(@Path("discussionId") discussionId: Long): Call<List<DiscussionComment>>

    // DiscussionController
    @GET("api/discussions")
    fun getAllDiscussions(): Call<List<Discussion>>

    @GET("api/discussions/{id}")
    fun getDiscussionById(@Path("id") id: Long): Call<Discussion>

    @POST("api/discussions")
    fun postDiscussion(@Body discussion: Discussion): Call<Discussion>

    @PUT("api/discussions/{id}")
    fun putDiscussion(@Path("id") id: Long, @Body discussion: Discussion): Call<Discussion>

    @DELETE("api/discussions/{id}")
    fun deleteDiscussion(@Path("id") id: Long): Call<Void>

    // DiscussionVoteController
    @GET("api/discussion-votes")
    fun getAllDiscussionVotes(): Call<List<DiscussionVote>>

    @GET("api/discussion-votes/{id}")
    fun getDiscussionVoteById(@Path("id") id: Long): Call<DiscussionVote>

    @POST("api/discussion-votes")
    fun postDiscussionVote(@Body vote: DiscussionVote): Call<DiscussionVote>

    @PUT("api/discussion-votes/{id}")
    fun putDiscussionVote(@Path("id") id: Long, @Body vote: DiscussionVote): Call<DiscussionVote>

    @DELETE("api/discussion-votes/{id}")
    fun deleteDiscussionVote(@Path("id") id: Long): Call<Void>

    @POST("api/articles-bookmark")
    fun bookmarkArticle(@Body bookmark: ArticleBookmark): Call<ArticleBookmark>

    @DELETE("api/articles-bookmark/{articleId}/user/{userId}")
    fun removeBookmark(@Path("articleId") articleId: Long, @Path("userId") userId: Long): Call<Void>

    @POST("api/discussions-bookmark")
    fun bookmarkDiscussion(@Body bookmark: DiscussionBookmark): Call<DiscussionBookmark>

    @DELETE("api/discussions-bookmark/{discussionId}/user/{userId}")
    fun removeDiscussionBookmark(@Path("discussionId") discussionId: Long, @Path("userId") userId: Long): Call<Void>

}