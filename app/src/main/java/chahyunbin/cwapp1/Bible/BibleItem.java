package chahyunbin.cwapp1.Bible;

public class BibleItem {
    private int book;
    private int chapter;
    private int verse;
    private String content;

    public BibleItem(int book, int chapter, int verse, String content) {
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        this.content = content;
    }
    public BibleItem(int verse, String content){
        super();
        this.verse = verse;
        this.content = content;

    }
    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getVerse() {
        return verse;
    }

    public void setVerse(int verse) {
        this.verse = verse;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
