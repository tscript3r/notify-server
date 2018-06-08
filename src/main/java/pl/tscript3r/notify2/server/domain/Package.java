package pl.tscript3r.notify2.server.domain;

public class Package {
	protected int id;
	protected long hostId;
	protected String url;
	protected Recipient recipient;

	public Package(int id, long hostId, String url, Recipient recipient) {
		this.id = id;
		this.hostId = hostId;
		this.url = url;
		this.recipient = recipient;
	}

	public long getHostId() {
		return hostId;
	}

	public Recipient getRecipient() {
		return recipient;
	}

	public int getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null)
			return (id == obj.hashCode()) ? true : false;
		return false;
	}
}
