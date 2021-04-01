package jd.server.util;

import java.util.Comparator;

public class PathUtils {
	
	private PathUtils() {
		throw new RuntimeException("cannot be instantiated.");
	};
	
	private final static String SEPARATOR = "/" ;
	
	public static String normalize(String path) {
		if(path == null) {
			path = "/" ;
		}else {
			path = path.trim();
			if(!path.startsWith(SEPARATOR)) {
				path = SEPARATOR + path;
			}
		}
		return path;
	}
	
	public static Comparator<String> longerAheadPathComparator = (path1,path2)->{
		if(path1.equals(path2)) {
			return 0 ;
		}else if(path1.startsWith(path2)) {
			return -1 ;
		}else if(path2.startsWith(path1)) {
			return 1 ;
		}else{
			return path1.compareTo(path2);
		}
	};
	
	
}
