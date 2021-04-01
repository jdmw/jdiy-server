package jd.server.util;

import java.util.Set;
import java.util.TreeSet;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class PathUtilsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testNormalize() {
		Assert.assertEquals("/",PathUtils.normalize(""));
		Assert.assertEquals("/",PathUtils.normalize("/"));
		Assert.assertEquals("/a",PathUtils.normalize("a"));
		Assert.assertEquals("/a",PathUtils.normalize("/a"));
	}

	@Test
	public void test_longgerAheadPathComparator() {
		Set<String> list = new TreeSet<>(PathUtils.longerAheadPathComparator);
		list.add("/a/b");
		list.add("/a");
		list.add("/b");
		list.add("/");
		System.out.println(list);
	}
}
