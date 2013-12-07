/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.splitPanel;

import java.util.List;

/**
 * This interface has to be implemented if you want to use the {@link SplitPanel}. This factory
 * offers three different methods that will be called by the {@link SplitPanel} internally. E.g. if
 * a frame should be splitted into two subframes the {@link SplitPanel} calls the method
 * {@link SPContentFactory#createContentForKey(String)} to create a new instance of the frame that
 * should be splitted.
 * @author Thomas Obenaus
 * @source SPContentFactory.java
 * @date 13.02.2012
 */
public interface SPContentFactory
{
	/**
	 * Returns a list of keys that represent the available content that can be created by this
	 * factory.
	 * @return
	 */
	public List<String> getContentKeys( );

	/**
	 * This method creates the content for a given key.
	 * @param key - the key identifying the content
	 * @param frameID - the id of the frame the content should be created for. To be able to
	 *            implement caching of frames content you need to know the frame the content is intended
	 *            for.
	 * @return
	 */
	public SPContent createContentForKey( String key, int frameID );

	/**
	 * This method returns a key for the default content a completely new subframe should be filled
	 * with.
	 * @return
	 */
	public String getDefaultContentKey( );

	/**
	 * Returns a name specifying the type of content this factory is able to provide.
	 * @return
	 */
	public String getName( );

	/**
	 * This method should return a unique frame-id. This method is called every time a new {@link SPFrame} is created. The id will be used
	 * to identify the new frame. For example the
	 * method {@link SPContentFactory#createContentForKey(String, int)} will be called with this
	 * frameID. So you can use this id to implement caching of frames content.
	 * @return
	 */
	public int getNewFrameID( );

	/**
	 * This method will be called if a {@link SPFrame} is deleted --> if its id is not needed any
	 * more. You can free this id.
	 * @param id
	 */
	public void freeFrameID( int id );
}
