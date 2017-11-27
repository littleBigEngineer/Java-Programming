public class Images extends Observer implements Media {
    @Override
    public void create() {
        System.out.println("IMAGE");
    }
    MediaFactory mediaFactory = new MediaFactory();
	public Images(Subject subject){
		this.subject = subject;
		this.subject.attach(this);
	}
	public Images(String string) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void update() {
		if(this.subject.getState().toString().equals(".jpg")){
            Media media = mediaFactory.getMedia("IMAGE");
            media.create();
        }
	}
}