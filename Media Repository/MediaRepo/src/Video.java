public class Video extends Observer implements Media{
    @Override
        public void create() {
            System.out.println("VIDEO");
        }
    MediaFactory mediaFactory = new MediaFactory();
	public Video(Subject subject){
		this.subject = subject;
		this.subject.attach(this);
	}
	@Override
	public void update() {
		if(this.subject.getState().toString().equals(".mp4")){
            Media media = mediaFactory.getMedia("VIEDO");
            media.create();
        }
	}

}