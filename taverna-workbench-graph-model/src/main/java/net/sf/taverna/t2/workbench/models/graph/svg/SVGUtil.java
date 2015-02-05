/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester
 *
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package net.sf.taverna.t2.workbench.models.graph.svg;

import static java.lang.Float.parseFloat;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static org.apache.batik.dom.svg.SVGDOMImplementation.getDOMImplementation;
import static org.apache.batik.util.SMILConstants.SMIL_ATTRIBUTE_NAME_ATTRIBUTE;
import static org.apache.batik.util.SMILConstants.SMIL_DUR_ATTRIBUTE;
import static org.apache.batik.util.SMILConstants.SMIL_FILL_ATTRIBUTE;
import static org.apache.batik.util.SMILConstants.SMIL_FREEZE_VALUE;
import static org.apache.batik.util.SMILConstants.SMIL_FROM_ATTRIBUTE;
import static org.apache.batik.util.SMILConstants.SMIL_TO_ATTRIBUTE;
import static org.apache.batik.util.SVGConstants.SVG_TYPE_ATTRIBUTE;
import static org.apache.batik.util.SVGConstants.SVG_X1_ATTRIBUTE;
import static org.apache.batik.util.SVGConstants.SVG_X2_ATTRIBUTE;
import static org.apache.batik.util.SVGConstants.SVG_Y1_ATTRIBUTE;
import static org.apache.batik.util.SVGConstants.SVG_Y2_ATTRIBUTE;
import static org.apache.batik.util.XMLResourceDescriptor.getXMLParserClassName;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;

import net.sf.taverna.t2.lang.io.StreamDevourer;
import net.sf.taverna.t2.workbench.configuration.workbench.WorkbenchConfiguration;
import net.sf.taverna.t2.workbench.models.graph.GraphShapeElement.Shape;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMAnimationElement;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;
//import org.apache.batik.transcoder.TranscoderException;
//import org.apache.batik.transcoder.svg2svg.PrettyPrinter;

/**
 * Utility methods.
 *
 * @author David Withers
 */
public class SVGUtil {
	private static final String C = "C";
	private static final String M = "M";
	private static final String SPACE = " ";
	private static final String COMMA = ",";
	public static final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
	private static final String SVG = "svg";
	private static final Logger logger = Logger.getLogger(SVGUtil.class);

	private static SAXSVGDocumentFactory docFactory;

	static {
		String parser = getXMLParserClassName();
		logger.info("Using XML parser " + parser);
		docFactory = new SAXSVGDocumentFactory(parser);
	}

	/**
	 * Creates a new SVGDocument.
	 * 
	 * @return a new SVGDocument
	 */
	public static SVGDocument createSVGDocument() {
		DOMImplementation impl = getDOMImplementation();
		return (SVGDocument) impl.createDocument(svgNS, SVG, null);
	}

	/**
	 * Converts a point in screen coordinates to a point in document
	 * coordinates.
	 * 
	 * @param locatable
	 * @param screenPoint
	 *            the point in screen coordinates
	 * @return the point in document coordinates
	 */
	public static SVGOMPoint screenToDocument(SVGLocatable locatable,
			SVGOMPoint screenPoint) {
		SVGMatrix mat = ((SVGLocatable) locatable.getFarthestViewportElement())
				.getScreenCTM().inverse();
		return (SVGOMPoint) screenPoint.matrixTransform(mat);
	}

	/**
	 * Writes SVG to the console. For debugging only.
	 *
	 * @param svgDocument
	 *            the document to output
	 */
//	public static void writeSVG(SVGDocument svgDocument) {
//		writeSVG(svgDocument, new OutputStreamWriter(System.out));
//	}

	/**
	 * Writes SVG to an output stream.
	 *
	 * @param svgDocument
	 *            the document to output
	 * @param writer
	 *            the stream to write the document to
	 */
//	public static void writeSVG(SVGDocument svgDocument, Writer writer) {
//		StringWriter sw = new StringWriter();
//		try {
//			Transformer transformer = TransformerFactory.newInstance().newTransformer();
//			Source src = new DOMSource(svgDocument.getDocumentElement());
//			transformer.transform(src, new StreamResult(sw));
//
//			PrettyPrinter pp = new PrettyPrinter();
//			pp.print(new StringReader(sw.toString()), writer);
//		} catch (TransformerException | TranscoderException | IOException e) {
//			e.printStackTrace(new PrintWriter(writer));
//		}
//	}

	/**
	 * Generates an SVGDocument from DOT text by calling out to GraphViz.
	 * 
	 * @param dotText
	 * @return an SVGDocument
	 * @throws IOException
	 */
	public static SVGDocument getSVG(String dotText,
			WorkbenchConfiguration workbenchConfiguration) throws IOException {
		String dotLocation = (String) workbenchConfiguration
				.getProperty("taverna.dotlocation");
		if (dotLocation == null)
			dotLocation = "dot";
		logger.debug("Invoking dot...");
		Process dotProcess = exec(dotLocation, "-Tsvg");
		StreamDevourer devourer = new StreamDevourer(
				dotProcess.getInputStream());
		devourer.start();
		try (PrintWriter out = new PrintWriter(dotProcess.getOutputStream(),
				true)) {
			out.print(dotText);
			out.flush();
		}

		String svgText = devourer.blockOnOutput();
		/*
		 * Avoid TAV-424, replace buggy SVG outputted by "modern" GraphViz
		 * versions. http://www.graphviz.org/bugs/b1075.html
		 * 
		 * Contributed by Marko Ullgren
		 */
		svgText = svgText.replaceAll("font-weight:regular",
				"font-weight:normal");
		logger.info(svgText);
		// Fake URI, just used for internal references like #fish
		return docFactory.createSVGDocument(
				"http://taverna.sf.net/diagram/generated.svg",
				new StringReader(svgText));
	}

	/**
	 * Generates DOT text with layout information from DOT text by calling out
	 * to GraphViz.
	 * 
	 * @param dotText
	 *            dot text
	 * @return dot text with layout information
	 * @throws IOException
	 */
	public static String getDot(String dotText,
			WorkbenchConfiguration workbenchConfiguration) throws IOException {
		String dotLocation = (String) workbenchConfiguration
				.getProperty("taverna.dotlocation");
		if (dotLocation == null)
			dotLocation = "dot";
		logger.debug("Invoking dot...");
		Process dotProcess = exec(dotLocation, "-Tdot", "-Glp=0,0");
		StreamDevourer devourer = new StreamDevourer(
				dotProcess.getInputStream());
		devourer.start();
		try (PrintWriter out = new PrintWriter(dotProcess.getOutputStream(),
				true)) {
			out.print(dotText);
			out.flush();
		}

		String dot = devourer.blockOnOutput();
		// logger.info(dot);
		return dot;
	}

	private static Process exec(String...args) throws IOException {
		Process p = Runtime.getRuntime().exec(args);
		/*
		 * Must create an error devourer otherwise stderr fills up and the
		 * process stalls!
		 */
		new StreamDevourer(p.getErrorStream()).start();
		return p;
	}

	/**
	 * Returns the hex value for a <code>Color</code>. If color is null "none"
	 * is returned.
	 *
	 * @param color
	 *            the <code>Color</code> to convert to hex code
	 * @return the hex value
	 */
	public static String getHexValue(Color color) {
		if (color == null)
			return "none";

		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(),
				color.getBlue());
	}

	/**
	 * Calculates the angle to rotate an arrow head to be placed on the end of a
	 * line.
	 *
	 * @param line
	 *            the line to calculate the arrow head angle from
	 * @return the angle to rotate an arrow head
	 */
	public static double calculateAngle(Element line) {
		float x1 = parseFloat(line.getAttribute(SVG_X1_ATTRIBUTE));
		float y1 = parseFloat(line.getAttribute(SVG_Y1_ATTRIBUTE));
		float x2 = parseFloat(line.getAttribute(SVG_X2_ATTRIBUTE));
		float y2 = parseFloat(line.getAttribute(SVG_Y2_ATTRIBUTE));
		return calculateAngle(x1, y1, x2, y2);
	}

	/**
	 * Calculates the angle to rotate an arrow head to be placed on the end of a
	 * line.
	 *
	 * @param pointList
	 *            the list of <code>Point</code>s to calculate the arrow head
	 *            angle from
	 * @return the angle to rotate an arrow head
	 */
	public static double calculateAngle(List<Point> pointList) {
		double angle = 0d;
		if (pointList.size() > 1) {
			int listSize = pointList.size();
			Point a = pointList.get(listSize - 2);
			Point b = pointList.get(listSize - 1);
			/*
			 * dot sometimes generates paths with the same point repeated at the
			 * end of the path, so move back along the path until two different
			 * points are found
			 */
			while (a.equals(b) && listSize > 2) {
				b = a;
				a = pointList.get(--listSize - 2);
			}
			angle = calculateAngle(a.x, a.y, b.x, b.y);
		}
		return angle;
	}

	/**
	 * Calculates the angle to rotate an arrow head to be placed on the end of a
	 * line.
	 * 
	 * @param x1
	 *            the x coordinate of the start of the line
	 * @param y1
	 *            the y coordinate of the start of the line
	 * @param x2
	 *            the x coordinate of the end of the line
	 * @param y2
	 *            the y coordinate of the end of the line
	 * @return the angle to rotate an arrow head
	 */
	public static double calculateAngle(float x1, float y1, float x2, float y2) {
		return atan2(y2 - y1, x2 - x1) * 180 / PI;
	}

	/**
	 * Calculates the points that make up the polygon for the specified
	 * {@link Shape}.
	 *
	 * @param shape
	 *            the <code>Shape</code> to calculate points for
	 * @param width
	 *            the width of the <code>Shape</code>
	 * @param height
	 *            the height of the <code>Shape</code>
	 * @return the points that make up the polygon for the specified
	 *         <code>Shape</code>
	 */
	public static String calculatePoints(Shape shape, int width, int height) {
		StringBuilder sb = new StringBuilder();
		switch (shape) {
		case BOX:
		case RECORD:
			addPoint(sb, 0, 0);
			addPoint(sb, width, 0);
			addPoint(sb, width, height);
			addPoint(sb, 0, height);
			break;
		case HOUSE:
			addPoint(sb, width / 2f, 0);
			addPoint(sb, width, height / 3f);
			addPoint(sb, width, height - 3);
			addPoint(sb, 0, height - 3);
			addPoint(sb, 0, height / 3f);
			break;
		case INVHOUSE:
			addPoint(sb, 0, 3);
			addPoint(sb, width, 3);
			addPoint(sb, width, height / 3f * 2f);
			addPoint(sb, width / 2f, height);
			addPoint(sb, 0, height / 3f * 2f);
			break;
		case TRIANGLE:
			addPoint(sb, width / 2f, 0);
			addPoint(sb, width, height);
			addPoint(sb, 0, height);
			break;
		case INVTRIANGLE:
			addPoint(sb, 0, 0);
			addPoint(sb, width, 0);
			addPoint(sb, width / 2f, height);
			break;
		default:
			// Nothing to do for the others
			break;
		}
		return sb.toString();
	}

	/**
	 * Appends x y coordinates to a <code>StringBuilder</code> in the format
	 * "x,y ".
	 * 
	 * @param stringBuilder
	 *            the <code>StringBuilder</code> to append the point to
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public static void addPoint(StringBuilder stringBuilder, float x, float y) {
		stringBuilder.append(x).append(COMMA).append(y).append(SPACE);
	}

	/**
	 * Converts a list of points into a string format for a cubic Bezier curve.
	 *
	 * For example, "M100,200 C100,100 250,100 250,200". See
	 * http://www.w3.org/TR/SVG11/paths.html#PathDataCubicBezierCommands.
	 *
	 * @param pointList
	 *            a list of points that describes a cubic Bezier curve
	 * @return a string that describes a cubic Bezier curve
	 */
	public static String getPath(List<Point> pointList) {
		StringBuilder sb = new StringBuilder();
		if (pointList != null && pointList.size() > 1) {
			Point firstPoint = pointList.get(0);
			sb.append(M).append(firstPoint.x).append(COMMA)
					.append(firstPoint.y);
			sb.append(SPACE);
			Point secontPoint = pointList.get(1);
			sb.append(C).append(secontPoint.x).append(COMMA)
					.append(secontPoint.y);
			for (int i = 2; i < pointList.size(); i++) {
				Point point = pointList.get(i);
				sb.append(SPACE).append(point.x).append(COMMA).append(point.y);
			}
		}
		return sb.toString();
	}

	/**
	 * Creates an animation element.
	 *
	 * @param graphController
	 *            the SVGGraphController to use to create the animation element
	 * @param elementType
	 *            the type of animation element to create
	 * @param attribute
	 *            the attribute that the animation should affect
	 * @param transformType
	 *            the type of transform - use null not creating a transform
	 *            animation
	 * @return an new animation element
	 */
	public static SVGOMAnimationElement createAnimationElement(
			SVGGraphController graphController, String elementType,
			String attribute, String transformType) {
		SVGOMAnimationElement animationElement = (SVGOMAnimationElement) graphController
				.createElement(elementType);
		animationElement.setAttribute(SMIL_ATTRIBUTE_NAME_ATTRIBUTE, attribute);
		if (transformType != null)
			animationElement.setAttribute(SVG_TYPE_ATTRIBUTE, transformType);
		animationElement.setAttribute(SMIL_FILL_ATTRIBUTE, SMIL_FREEZE_VALUE);
		return animationElement;
	}

	/**
	 * Adds an animation to the SVG element and starts the animation.
	 *
	 * @param animate
	 *            that animation element
	 * @param element
	 *            the element to animate
	 * @param duration
	 *            the duration of the animation in milliseconds
	 * @param from
	 *            the starting point for the animation, can be null
	 * @param to
	 *            the end point for the animation, cannot be null
	 */
	public static void animate(SVGOMAnimationElement animate, SVGElement element, int duration,
			String from, String to) {
		animate.setAttribute(SMIL_DUR_ATTRIBUTE, duration + "ms");
		if (from != null)
			animate.setAttribute(SMIL_FROM_ATTRIBUTE, from);
		animate.setAttribute(SMIL_TO_ATTRIBUTE, to);
		element.appendChild(animate);
		try {
			animate.beginElement();
		} catch (NullPointerException e) {
		}
	}

	/**
	 * Adjusts the length of <code>pointList</code> by adding or removing points
	 * to make the length equal to <code>size</code>. If <code>pointList</code>
	 * is shorter than <code>size</code> the last point is repeated. If
	 * <code>pointList</code> is longer than <code>size</code> points at the end
	 * of the list are removed.
	 *
	 * @param pointList
	 *            the path to adjust
	 * @param size
	 *            the required size for <code>pointList</code>
	 */
	public static void adjustPathLength(List<Point> pointList, int size) {
		if (pointList.size() < size) {
			Point lastPoint = pointList.get(pointList.size() - 1);
			for (int i = pointList.size(); i < size; i++)
				pointList.add(lastPoint);
		} else if (pointList.size() > size) {
			for (int i = pointList.size(); i > size; i--)
				pointList.remove(i - 1);
		}
	}
}
