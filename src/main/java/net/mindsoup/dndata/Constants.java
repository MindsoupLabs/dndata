package net.mindsoup.dndata;

/**
 * Created by Valentijn on 8-8-2019
 */
public abstract class Constants {

	public abstract static class Rights {
		public abstract static class PF2 {
			public static final String EDIT = "ROLE_PF2_EDIT";
			public static final String REVIEW = "ROLE_PF2_REVIEW";
			public static final String BOOKS = "ROLE_PF2_BOOKS";
			public static final String PUBLISH = "ROLE_PF2_PUBLISH";
			public static final String MANAGE_USERS = "ROLE_PF2_MANAGE_USERS";
		}
	}

	public abstract static class Comments {
		public static final String AUTO_COMMENT_PREFIX = "(Auto generated comment) ";
		public static final String OBJECT_CREATED = "Data object created";
		public static final String READY_FOR_REVIEW = "Ready for review";
	}

	public abstract static class Status {
		public abstract static class Color {
			public static final String CREATED = "bg-blue";
			public static final String EDITING = "bg-yellow";
			public static final String AWAITING_REVIEW = "bg-purple";
			public static final String REVIEWED = "bg-green";
			public static final String PUBLISHED = "bg-red";
			public static final String DELETED = "bg-black";
		}

		public abstract static class Icon {
			public static final String CREATED = "fa-star-o";
			public static final String EDITING = "fa-pencil";
			public static final String AWAITING_REVIEW = "fa-square-o";
			public static final String REVIEWED = "fa-check-square-o";
			public static final String PUBLISHED = "fa-upload";
			public static final String DELETED = "fa-trash-o";
		}
	}
}
