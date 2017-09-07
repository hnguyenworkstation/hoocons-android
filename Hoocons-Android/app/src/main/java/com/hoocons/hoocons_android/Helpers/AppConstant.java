package com.hoocons.hoocons_android.Helpers;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class AppConstant {
    public static int EMOTICON_CLICK_TEXT = 1;
    public static int EMOTICON_CLICK_BIGIMAGE = 2;

    // AWS S3 Path constants
    public static final String CHANNEL_PATH = "channels";

    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";

    public static final String GIPHY_PUBLIC_KEY = "dc6zaTOxFJmzC";

    // Media type
    public static final String MEDIA_TYPE_IMAGE = "Img";
    public static final String MEDIA_TYPE_GIF = "Gif";
    public static final String MEDIA_TYPE_VIDEO = "Vid";
    public static final String MEDIA_TYPE_MOMENT = "Mmt";

    // General Event Type
    public static final String TYPE_STORY = "story";
    public static final String TYPE_QUESTION = "question";
    public static final String TYPE_QUOTE = "quote";
    public static final String TYPE_WISH = "wish";
    public static final String TYPE_CHECKING = "checking";
    public static final String TYPE_INVITATION = "invitation";
    public static final String TYPE_ASK = "ask";
    public static final String TYPE_PROMOTION = "promotion";

    // Event type
    public static final String EVENT_TYPE_TEXT = "Txt";
    public static final String EVENT_TYPE_SINGLE_IMAGE = "Sigm";
    public static final String EVENT_TYPE_MULT_IMAGE = "Migm";
    public static final String EVENT_TYPE_SINGLE_VIDEO = "Svid";
    public static final String EVENT_TYPE_SINGLE_VOICE = "Svoi";
    public static final String EVENT_TYPE_SINGLE_GIF = "Sgif";
    public static final String EVENT_TYPE_CHECK_IN = "Chk";

    // CommentType
    public static final String COMMENT_TYPE_TEXT = "Ctxt";
    public static final String COMMENT_TYPE_IMAGE = "Cimg";
    public static final String COMMENT_TYPE_GIF = "Cgif";

    // Chat Message Type
    public static final String MESSAGE_TYPE_TEXT = "MTEXT";
    public static final String MESSAGE_TYPE_IMAGE = "MIMAGE";
    public static final String MESSAGE_TYPE_STICKER= "MSTICKER";
    public static final String MESSAGE_TYPE_GIF = "MGIF";
    public static final String MESSAGE_TYPE_VIDEO = "MVIDEO";
    public static final String MESSAGE_TYPE_FILE = "MFILE";
    public static final String MESSAGE_TYPE_LOCATION = "MLOCATION";
    public static final String MESSAGE_TYPE_CONTACT = "MCONTACT";
    public static final String MESSAGE_TYPE_VOICE = "MVOICE";

    public static final String CHATROOM_TYPE_SINGLE = "ROOM_SINGLE";
    public static final String CHATROOM_TYPE_MULTI = "ROOM_MULTI";

    // Constant TAGs for JOBS
    public static final String CREATE_NEW_CHANNEL_TAG = "create_new_channel";
    public static final String CANCEL_FRIEND_REQUEST_TAG = "cancel_friend_request";
    public static final String FRIEND_REQUEST_TAG = "friend_request";
    public static final String UNFRIEND_REQUEST_TAG = "unfriend_request";

    // Constant S3 Folder Path
    public static final String PROFILE_PATH = "profiles";
}
