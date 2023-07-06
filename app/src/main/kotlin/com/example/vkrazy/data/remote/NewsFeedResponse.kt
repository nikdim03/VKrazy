data class NewsFeedResponse(
    val response: NewsFeedData
)

data class NewsFeedData(
    val items: List<PostItem>
)

data class PostItem(
    val type: String,
    val source_id: Long,
    val date: Long,
    val short_text_rate: Double,
    val donut: Donut,
    val comments: CommentStats,
    val marked_as_ads: Int,
    val can_set_category: Boolean,
    val can_doubt_category: Boolean,
    val attachments: List<Attachment>,
    val id: Int,
    val is_favorite: Boolean,
    val likes: LikeStats,
    val owner_id: Long,
    val post_id: Int,
    val post_source: PostSource,
    val post_type: String,
    val reposts: RepostStats,
    val text: String,
    val views: ViewStats
)

data class Attachment(
    val type: String,
    val photo: Photo?,
    val video: Video?
)

data class Photo(
    val album_id: Int,
    val date: Long,
    val id: Int,
    val owner_id: Long,
    val access_key: String,
    val post_id: Int,
    val sizes: List<PhotoSize>
)

data class PhotoSize(
    val height: Int,
    val type: String,
    val width: Int,
    val url: String
)

data class Video(
    val response_type: String,
    val can_comment: Int,
    val can_like: Int,
    val can_repost: Int,
    val can_subscribe: Int,
    val can_add_to_faves: Int,
    val can_add: Int,
    val comments: Int,
    val date: Long,
    val description: String,
    val duration: Int,
    val image: List<VideoImage>,
    val first_frame: List<VideoImage>,
    val width: Int,
    val height: Int,
    val id: Int,
    val owner_id: Long,
    val title: String,
    val is_favorite: Boolean,
    val track_code: String,
    val repeat: Int,
    val type: String,
    val views: Int,
    val local_views: Int,
    val can_dislike: Int
)

data class VideoImage(
    val url: String,
    val width: Int,
    val height: Int,
    val with_padding: Int?
)

data class Donut(
    val is_donut: Boolean
)

data class CommentStats(
    val can_post: Int,
    val count: Int,
    val groups_can_post: Boolean?
)

data class LikeStats(
    val can_like: Int,
    val count: Int,
    val user_likes: Int,
    val can_publish: Int,
    val repost_disabled: Boolean
)

data class PostSource(
    val type: String?,
    val platform: String?
)

data class RepostStats(
    val count: Int,
    val user_reposted: Int
)

data class ViewStats(
    val count: Int
)
