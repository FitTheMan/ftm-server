package com.ftm.server.common.consts;

public class StaticConsts {

    public static final int BEAUTY_CATEGORY_MAX_SCORE = 20;
    public static final int HYGIENE_CATEGORY_MAX_SCORE = 14;
    public static final int HAIR_CATEGORY_MAX_SCORE = 11;
    public static final int WORKOUT_CATEGORY_MAX_SCORE = 40;
    public static final int FASHION_CATEGORY_MAX_SCORE = 14;
    public static final int MINIMUM_DAYS_BETWEEN_TESTS = 7;
    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_GRANT_TYPE = "authorization_code";
    public static final String CLIENT_SESSION_COOKIE_NAME = "SESSION";
    public static final String PENDING_SOCIAL_USER_SESSION_KEY = "PENDING_SOCIAL_USER_INFO";
    public static final int PENDING_SOCIAL_USER_SESSION_TTL = 300; //  5분
    public static final String GROOMING_TESTS_INFO_CACHE_NAME = "ftm:grooming:tests:info";
    public static final String GROOMING_TESTS_INFO_CACHE_KEY_ALL = "'all'";

    public static final String TRENDING_POSTS_CACHE_NAME = "ftm:posts:trend";
    public static final String TRENDING_POSTS_CACHE_KEY_ALL = "'all'";

    public static final String USER_PICK_POPULAR_POSTS_CACHE_NAME = "ftm:posts:userpick:popular";
    public static final String USER_PICK_POPULAR_POSTS_CACHE_KEY_ALL = "'all'";

    public static final String USER_PICK_BIBLE_POSTS_CACHE_NAME = "ftm:posts:userpick:bible";
    public static final String USER_PICK_BIBLE_POSTS_CACHE_KEY_ALL = "'all'";

    public static final String USER_PICK_TOP_BOOKMARK_POSTS_CACHE_NAME =
            "ftm:posts:userpick:top-bookmarks";
    public static final String USER_PICK_TOP_BOOKMARK_POSTS_CACHE_KEY_ALL = "'all'";

    public static final String USER_PICK_STORY_POPULAR_POSTS_CACHE_NAME =
            "ftm:posts:userpick:top-bookmarks";
    public static final String USER_PICK_STORY_POPULAR_POSTS_CACHE_KEY_ALL = "'all'";
}
