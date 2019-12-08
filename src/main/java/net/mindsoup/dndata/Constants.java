package net.mindsoup.dndata;

/**
 * Created by Valentijn on 8-8-2019
 */
public abstract class Constants {

	public abstract static class SQL {
		public static final String GET_ALL_OBJECTS_IN_BOOK_BY_STATUS = "SELECT * FROM (SELECT o.id, MAX(o.revision) AS revision, o.json, o.schema_version, o.name, o.type, o.book_id, SUBSTRING_INDEX(GROUP_CONCAT(s.status ORDER BY s.id DESC), ',', 1) AS status, MAX(s.id) AS statusId FROM objects AS o LEFT JOIN object_status AS s ON o.id = s.object_id AND o.revision = s.object_revision GROUP BY s.object_id) AS t WHERE t.book_id = :bookId AND t.status IN :statuses ORDER BY type ASC, name ASC";
	}

	public abstract static class Rights {
		public static final String EDIT = "EDIT";
		public static final String REVIEW = "REVIEW";
		public static final String BOOKS = "BOOKS";
		public static final String PUBLISH = "PUBLISH";
		public static final String MANAGE_USERS = "MANAGE_USERS";

		public abstract static class PF2 {
			private static final String prefix = "ROLE_PF2_";
			public static final String EDIT = prefix + Rights.EDIT;
			public static final String REVIEW = prefix + Rights.REVIEW;
			public static final String BOOKS = prefix + Rights.BOOKS;
			public static final String PUBLISH = prefix + Rights.PUBLISH;
			public static final String MANAGE_USERS = prefix + Rights.MANAGE_USERS;
		}
	}

	public abstract static class Comments {
		public static final String AUTO_COMMENT_PREFIX = "(Auto generated comment) ";
		public static final String OBJECT_CREATED = "Data object created";
		public static final String READY_FOR_REVIEW = "Submitted for review";
		public static final String REVIEW_APPROVED = "Review approved";
		public static final String PUBLISHED = "Published";
		public static final String REOPENED = "Reopened";
		public static final String DELETED = "Deleted";
	}

	public abstract static class Status {
		public abstract static class Color {
			public static final String CREATED = "bg-fuchsia";
			public static final String EDITING = "bg-yellow";
			public static final String AWAITING_REVIEW = "bg-purple";
			public static final String REVIEWED = "bg-blue";
			public static final String PUBLISHED = "bg-green";
			public static final String DELETED = "bg-red";
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

	public abstract static class Collections {
		public static final String COLLECTION_ALL = "all";
		public static final String BOOKS = "books";
		public static final String COLLECTIONS = "collections";
	}

	public abstract static class Files {
		public static final String TEMP_PREFIX = "temp_";
		public static final String JSON_SUFFIX = ".json";
		public static final String ZIP_SUFFIX = ".zip";
		public static final String LEGAL_TEXT = "legal.txt";
	}
}
