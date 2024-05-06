package il.meuhedet.temielderpeople.api.dto;

public class ArticleDTO {

	String title;
	String description;
	String content;
	String url;
	String image;
	String publishedAt;


	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public String getImage() {
		return image;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return "ArticleDTO{" +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", url='" + url + '\'' +
				", urlToImage='" + image + '\'' +
				", publishedAt='" + publishedAt + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}
