package uzuzjmd.competence.evidence.service.moodle;

/**
 * DTOs für den Moodle REST-Service
 *
 *
 * DTOs for the moodle rest service
 * 
 * @author julian
 * 
 */
public class Content {
	private String author; // s //Content owner
	private String content;// s Optional; // //Raw content, will be used when
	private String filename; // filename
	private String filepath; // filepath
	private int filesize; // filesize
	private String fileurl; // s Optional //downloadable file url
	private String license; // Content license

	private int sortorder; // Content sort order

	// type is
	// content
	private int timecreated; // Time created

	private int timemodified; // Time modified

	private String type; // a file or a folder or external link

	private int userid; // User who added this content to moodle

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public String getFilename() {
		return filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public int getFilesize() {
		return filesize;
	}

	public String getFileurl() {
		return fileurl;
	}

	public String getLicense() {
		return license;
	}

	public int getSortorder() {
		return sortorder;
	}

	public int getTimecreated() {
		return timecreated;
	}

	public int getTimemodified() {
		return timemodified;
	}

	public String getType() {
		return type;
	}

	public int getUserid() {
		return userid;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public void setTimecreated(int timecreated) {
		this.timecreated = timecreated;
	}

	public void setTimemodified(int timemodified) {
		this.timemodified = timemodified;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}
}
