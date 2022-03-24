package userinfo;

public class User {

	 private int id;
	    private String name;
	    private boolean isOnline;

	    public User(String name, boolean isOnline) {
	        this.name = name;
	        this.isOnline = isOnline;
	    }

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isOnline() {
			return isOnline;
		}

		public void setOnline(boolean isOnline) {
			this.isOnline = isOnline;
		}
	    
}
