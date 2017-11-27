public class Music extends Observer implements Media {
	@Override
        public void create() {
    	
        }
        
    MediaFactory mediaFactory = new MediaFactory();
    public Music(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }
    @Override
    public void update() {
        if(this.subject.getState().toString().equals(".mp3")){
            Media media = mediaFactory.getMedia("MUSIC");
            System.out.println("MUSIC");
            media.create();
        }
    }
}