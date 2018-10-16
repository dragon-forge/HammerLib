package com.zeitheron.hammercore.client.utils;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface IImagePreprocessor
{
	BufferedImage process(BufferedImage image);
}