# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn org.apache.batik.anim.dom.SAXSVGDocumentFactory
-dontwarn org.apache.batik.bridge.BridgeContext
-dontwarn org.apache.batik.bridge.DocumentLoader
-dontwarn org.apache.batik.bridge.GVTBuilder
-dontwarn org.apache.batik.bridge.UserAgent
-dontwarn org.apache.batik.bridge.UserAgentAdapter
-dontwarn org.apache.batik.util.XMLResourceDescriptor
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn org.osgi.framework.Bundle
-dontwarn org.osgi.framework.BundleContext
-dontwarn org.osgi.framework.FrameworkUtil
-dontwarn org.osgi.framework.ServiceReference

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode generic signatures are stripped for classes that are not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Keep all classes and methods in the specified package
-keep class com.manipur.khannasi.** { *; }

# Keep all classes and methods in the androidx.recyclerview.widget package
-keep class androidx.recyclerview.widget.** { *; }

# Keep all classes and methods in the androidx.constraintlayout.widget package
-keep class androidx.constraintlayout.widget.** { *; }

# Keep all classes and methods in the androidx.appcompat.widget package
-keep class androidx.appcompat.widget.** { *; }

# Keep all classes and methods in the androidx.core package
-keep class androidx.core.** { *; }

# Keep all classes and methods in the androidx.room package
-keep class androidx.room.** { *; }

# Keep all classes and methods in the retrofit2 package
-keep class retrofit2.** { *; }

# Keep all classes and methods in the okhttp3 package
-keep class okhttp3.** { *; }

# Keep all classes and methods in the com.squareup.picasso package
-keep class com.squareup.picasso.** { *; }

# Keep all classes and methods in the com.fasterxml.jackson package
-keep class com.fasterxml.jackson.** { *; }

# Keep all classes and methods in the com.android.volley package
-keep class com.android.volley.** { *; }

# Keep all classes and methods in the com.bumptech.glide package
-keep class com.bumptech.glide.** { *; }

# Keep all classes and methods in the org.apache.commons.net package
-keep class org.apache.commons.net.** { *; }

# Keep all classes and methods in the org.apache.poi package
-keep class org.apache.poi.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer package
-keep class com.github.barteksc.pdfviewer.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.source package
-keep class com.github.barteksc.pdfviewer.source.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.util package
-keep class com.github.barteksc.pdfviewer.util.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.exception package
-keep class com.github.barteksc.pdfviewer.exception.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.model package
-keep class com.github.barteksc.pdfviewer.model.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.listener package
-keep class com.github.barteksc.pdfviewer.listener.** { *; }

# Keep all classes and methods in the com.github.barteksc.pdfviewer.scroll package
-keep class com.github.barteksc.pdfviewer.scroll.** { *; }

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn de.rototor.pdfbox.graphics2d.IPdfBoxGraphics2DFontTextDrawer$IFontTextDrawerEnv
-dontwarn de.rototor.pdfbox.graphics2d.IPdfBoxGraphics2DFontTextDrawer
-dontwarn de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D
-dontwarn de.rototor.pdfbox.graphics2d.PdfBoxGraphics2DFontTextDrawer
-dontwarn java.awt.AlphaComposite
-dontwarn java.awt.BasicStroke
-dontwarn java.awt.Color
-dontwarn java.awt.Composite
-dontwarn java.awt.Dimension
-dontwarn java.awt.Font
-dontwarn java.awt.FontFormatException
-dontwarn java.awt.FontMetrics
-dontwarn java.awt.GradientPaint
-dontwarn java.awt.Graphics2D
-dontwarn java.awt.Graphics
-dontwarn java.awt.GraphicsConfiguration
-dontwarn java.awt.GraphicsDevice
-dontwarn java.awt.GraphicsEnvironment
-dontwarn java.awt.Image
-dontwarn java.awt.Insets
-dontwarn java.awt.LinearGradientPaint
-dontwarn java.awt.MultipleGradientPaint$ColorSpaceType
-dontwarn java.awt.MultipleGradientPaint$CycleMethod
-dontwarn java.awt.MultipleGradientPaint
-dontwarn java.awt.Paint
-dontwarn java.awt.PaintContext
-dontwarn java.awt.Polygon
-dontwarn java.awt.RadialGradientPaint
-dontwarn java.awt.Rectangle
-dontwarn java.awt.RenderingHints$Key
-dontwarn java.awt.RenderingHints
-dontwarn java.awt.Shape
-dontwarn java.awt.Stroke
-dontwarn java.awt.TexturePaint
-dontwarn java.awt.Toolkit
-dontwarn java.awt.color.ColorSpace
-dontwarn java.awt.font.FontRenderContext
-dontwarn java.awt.font.GlyphVector
-dontwarn java.awt.font.LineBreakMeasurer
-dontwarn java.awt.font.TextLayout
-dontwarn java.awt.geom.AffineTransform
-dontwarn java.awt.geom.Arc2D$Double
-dontwarn java.awt.geom.Arc2D$Float
-dontwarn java.awt.geom.Area
-dontwarn java.awt.geom.Dimension2D
-dontwarn java.awt.geom.Ellipse2D$Double
-dontwarn java.awt.geom.GeneralPath
-dontwarn java.awt.geom.IllegalPathStateException
-dontwarn java.awt.geom.Line2D$Double
-dontwarn java.awt.geom.Line2D
-dontwarn java.awt.geom.Path2D$Double
-dontwarn java.awt.geom.Path2D
-dontwarn java.awt.geom.PathIterator
-dontwarn java.awt.geom.Point2D$Double
-dontwarn java.awt.geom.Point2D
-dontwarn java.awt.geom.Rectangle2D$Double
-dontwarn java.awt.geom.Rectangle2D$Float
-dontwarn java.awt.geom.Rectangle2D
-dontwarn java.awt.geom.RoundRectangle2D$Double
-dontwarn java.awt.geom.RoundRectangle2D$Float
-dontwarn java.awt.image.AffineTransformOp
-dontwarn java.awt.image.BufferedImage
-dontwarn java.awt.image.BufferedImageOp
-dontwarn java.awt.image.ColorModel
-dontwarn java.awt.image.ComponentColorModel
-dontwarn java.awt.image.DirectColorModel
-dontwarn java.awt.image.ImageObserver
-dontwarn java.awt.image.IndexColorModel
-dontwarn java.awt.image.PackedColorModel
-dontwarn java.awt.image.Raster
-dontwarn java.awt.image.RenderedImage
-dontwarn java.awt.image.RescaleOp
-dontwarn java.awt.image.SampleModel
-dontwarn java.awt.image.WritableRaster
-dontwarn java.awt.image.renderable.RenderableImage
-dontwarn java.beans.ConstructorProperties
-dontwarn java.beans.Transient
-dontwarn javax.imageio.ImageIO
-dontwarn javax.imageio.ImageReadParam
-dontwarn javax.imageio.ImageReader
-dontwarn javax.imageio.ImageTypeSpecifier
-dontwarn javax.imageio.metadata.IIOMetadata
-dontwarn javax.imageio.stream.ImageInputStream
-dontwarn javax.imageio.stream.MemoryCacheImageInputStream
-dontwarn javax.swing.JLabel
-dontwarn javax.xml.crypto.AlgorithmMethod
-dontwarn javax.xml.crypto.Data
-dontwarn javax.xml.crypto.KeySelector$Purpose
-dontwarn javax.xml.crypto.KeySelector
-dontwarn javax.xml.crypto.KeySelectorException
-dontwarn javax.xml.crypto.KeySelectorResult
-dontwarn javax.xml.crypto.MarshalException
-dontwarn javax.xml.crypto.OctetStreamData
-dontwarn javax.xml.crypto.URIDereferencer
-dontwarn javax.xml.crypto.URIReference
-dontwarn javax.xml.crypto.URIReferenceException
-dontwarn javax.xml.crypto.XMLCryptoContext
-dontwarn javax.xml.crypto.XMLStructure
-dontwarn javax.xml.crypto.dom.DOMStructure
-dontwarn javax.xml.crypto.dsig.CanonicalizationMethod
-dontwarn javax.xml.crypto.dsig.DigestMethod
-dontwarn javax.xml.crypto.dsig.Manifest
-dontwarn javax.xml.crypto.dsig.Reference
-dontwarn javax.xml.crypto.dsig.SignatureMethod
-dontwarn javax.xml.crypto.dsig.SignatureProperties
-dontwarn javax.xml.crypto.dsig.SignatureProperty
-dontwarn javax.xml.crypto.dsig.SignedInfo
-dontwarn javax.xml.crypto.dsig.Transform
-dontwarn javax.xml.crypto.dsig.TransformException
-dontwarn javax.xml.crypto.dsig.TransformService
-dontwarn javax.xml.crypto.dsig.XMLObject
-dontwarn javax.xml.crypto.dsig.XMLSignContext
-dontwarn javax.xml.crypto.dsig.XMLSignature
-dontwarn javax.xml.crypto.dsig.XMLSignatureException
-dontwarn javax.xml.crypto.dsig.XMLSignatureFactory
-dontwarn javax.xml.crypto.dsig.XMLValidateContext
-dontwarn javax.xml.crypto.dsig.dom.DOMSignContext
-dontwarn javax.xml.crypto.dsig.dom.DOMValidateContext
-dontwarn javax.xml.crypto.dsig.keyinfo.KeyInfo
-dontwarn javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
-dontwarn javax.xml.crypto.dsig.keyinfo.KeyValue
-dontwarn javax.xml.crypto.dsig.keyinfo.X509Data
-dontwarn javax.xml.crypto.dsig.keyinfo.X509IssuerSerial
-dontwarn javax.xml.crypto.dsig.spec.C14NMethodParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.DigestMethodParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.TransformParameterSpec
-dontwarn javax.xml.stream.Location
-dontwarn javax.xml.stream.XMLEventFactory
-dontwarn javax.xml.stream.XMLInputFactory
-dontwarn javax.xml.stream.XMLOutputFactory
-dontwarn javax.xml.stream.XMLStreamException
-dontwarn javax.xml.stream.XMLStreamReader
-dontwarn javax.xml.stream.events.Namespace
-dontwarn net.sf.saxon.Configuration
-dontwarn net.sf.saxon.dom.DOMNodeWrapper
-dontwarn net.sf.saxon.dom.DocumentWrapper
-dontwarn net.sf.saxon.dom.NodeOverNodeInfo
-dontwarn net.sf.saxon.lib.ConversionRules
-dontwarn net.sf.saxon.ma.map.HashTrieMap
-dontwarn net.sf.saxon.om.GroundedValue
-dontwarn net.sf.saxon.om.Item
-dontwarn net.sf.saxon.om.NodeInfo
-dontwarn net.sf.saxon.om.Sequence
-dontwarn net.sf.saxon.om.SequenceTool
-dontwarn net.sf.saxon.om.StructuredQName
-dontwarn net.sf.saxon.query.DynamicQueryContext
-dontwarn net.sf.saxon.query.StaticQueryContext
-dontwarn net.sf.saxon.query.XQueryExpression
-dontwarn net.sf.saxon.str.StringView
-dontwarn net.sf.saxon.str.UnicodeString
-dontwarn net.sf.saxon.sxpath.IndependentContext
-dontwarn net.sf.saxon.sxpath.XPathDynamicContext
-dontwarn net.sf.saxon.sxpath.XPathEvaluator
-dontwarn net.sf.saxon.sxpath.XPathExpression
-dontwarn net.sf.saxon.sxpath.XPathStaticContext
-dontwarn net.sf.saxon.sxpath.XPathVariable
-dontwarn net.sf.saxon.tree.wrapper.VirtualNode
-dontwarn net.sf.saxon.type.BuiltInAtomicType
-dontwarn net.sf.saxon.type.ConversionResult
-dontwarn net.sf.saxon.value.AnyURIValue
-dontwarn net.sf.saxon.value.AtomicValue
-dontwarn net.sf.saxon.value.BigDecimalValue
-dontwarn net.sf.saxon.value.BigIntegerValue
-dontwarn net.sf.saxon.value.BooleanValue
-dontwarn net.sf.saxon.value.CalendarValue
-dontwarn net.sf.saxon.value.DateTimeValue
-dontwarn net.sf.saxon.value.DateValue
-dontwarn net.sf.saxon.value.DoubleValue
-dontwarn net.sf.saxon.value.DurationValue
-dontwarn net.sf.saxon.value.FloatValue
-dontwarn net.sf.saxon.value.GDateValue
-dontwarn net.sf.saxon.value.GDayValue
-dontwarn net.sf.saxon.value.GMonthDayValue
-dontwarn net.sf.saxon.value.GMonthValue
-dontwarn net.sf.saxon.value.GYearMonthValue
-dontwarn net.sf.saxon.value.GYearValue
-dontwarn net.sf.saxon.value.HexBinaryValue
-dontwarn net.sf.saxon.value.Int64Value
-dontwarn net.sf.saxon.value.ObjectValue
-dontwarn net.sf.saxon.value.QNameValue
-dontwarn net.sf.saxon.value.SaxonDuration
-dontwarn net.sf.saxon.value.SaxonXMLGregorianCalendar
-dontwarn net.sf.saxon.value.StringValue
-dontwarn net.sf.saxon.value.TimeValue
-dontwarn org.apache.batik.bridge.ViewBox
-dontwarn org.apache.batik.dom.GenericDOMImplementation
-dontwarn org.apache.batik.ext.awt.RenderingHintsKeyExt
-dontwarn org.apache.batik.ext.awt.image.renderable.ClipRable8Bit
-dontwarn org.apache.batik.ext.awt.image.renderable.ClipRable
-dontwarn org.apache.batik.ext.awt.image.renderable.Filter
-dontwarn org.apache.batik.gvt.GraphicsNode
-dontwarn org.apache.batik.parser.DefaultLengthHandler
-dontwarn org.apache.batik.parser.LengthHandler
-dontwarn org.apache.batik.parser.LengthParser
-dontwarn org.apache.batik.svggen.DOMTreeManager
-dontwarn org.apache.batik.svggen.DefaultExtensionHandler
-dontwarn org.apache.batik.svggen.ExtensionHandler
-dontwarn org.apache.batik.svggen.SVGColor
-dontwarn org.apache.batik.svggen.SVGGeneratorContext$GraphicContextDefaults
-dontwarn org.apache.batik.svggen.SVGGeneratorContext
-dontwarn org.apache.batik.svggen.SVGGraphics2D
-dontwarn org.apache.batik.svggen.SVGIDGenerator
-dontwarn org.apache.batik.svggen.SVGPaintDescriptor
-dontwarn org.apache.batik.svggen.SVGTexturePaint
-dontwarn org.apache.jcp.xml.dsig.internal.dom.ApacheNodeSetData
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMKeyInfo
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMReference
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMSignedInfo
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMSubTreeData
-dontwarn org.apache.pdfbox.pdmodel.PDDocument
-dontwarn org.apache.pdfbox.pdmodel.PDPage
-dontwarn org.apache.pdfbox.pdmodel.PDPageContentStream
-dontwarn org.apache.pdfbox.pdmodel.common.PDRectangle
-dontwarn org.apache.pdfbox.pdmodel.font.PDFont
-dontwarn org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
-dontwarn org.apache.xml.security.Init
-dontwarn org.apache.xml.security.c14n.Canonicalizer
-dontwarn org.apache.xml.security.signature.XMLSignatureInput
-dontwarn org.apache.xml.security.utils.XMLUtils
-dontwarn org.bouncycastle.asn1.ASN1Encodable
-dontwarn org.bouncycastle.asn1.ASN1IA5String
-dontwarn org.bouncycastle.asn1.ASN1InputStream
-dontwarn org.bouncycastle.asn1.ASN1Integer
-dontwarn org.bouncycastle.asn1.ASN1Object
-dontwarn org.bouncycastle.asn1.ASN1ObjectIdentifier
-dontwarn org.bouncycastle.asn1.ASN1OctetString
-dontwarn org.bouncycastle.asn1.ASN1Primitive
-dontwarn org.bouncycastle.asn1.DERTaggedObject
-dontwarn org.bouncycastle.asn1.cmp.PKIFailureInfo
-dontwarn org.bouncycastle.asn1.nist.NISTObjectIdentifiers
-dontwarn org.bouncycastle.asn1.ocsp.ResponderID
-dontwarn org.bouncycastle.asn1.x500.X500Name
-dontwarn org.bouncycastle.asn1.x509.CRLDistPoint
-dontwarn org.bouncycastle.asn1.x509.DistributionPoint
-dontwarn org.bouncycastle.asn1.x509.DistributionPointName
-dontwarn org.bouncycastle.asn1.x509.Extension
-dontwarn org.bouncycastle.asn1.x509.GeneralName
-dontwarn org.bouncycastle.asn1.x509.GeneralNames
-dontwarn org.bouncycastle.asn1.x509.X509ObjectIdentifiers
-dontwarn org.bouncycastle.cert.X509CertificateHolder
-dontwarn org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
-dontwarn org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils
-dontwarn org.bouncycastle.cert.ocsp.BasicOCSPResp
-dontwarn org.bouncycastle.cert.ocsp.OCSPResp
-dontwarn org.bouncycastle.cert.ocsp.RespID
-dontwarn org.bouncycastle.cms.CMSSignatureAlgorithmNameGenerator
-dontwarn org.bouncycastle.cms.DefaultCMSSignatureAlgorithmNameGenerator
-dontwarn org.bouncycastle.cms.SignerId
-dontwarn org.bouncycastle.cms.SignerInformationVerifier
-dontwarn org.bouncycastle.cms.bc.BcRSASignerInfoVerifierBuilder
-dontwarn org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder
-dontwarn org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder
-dontwarn org.bouncycastle.operator.DigestAlgorithmIdentifierFinder
-dontwarn org.bouncycastle.operator.DigestCalculatorProvider
-dontwarn org.bouncycastle.operator.SignatureAlgorithmIdentifierFinder
-dontwarn org.bouncycastle.operator.bc.BcDigestCalculatorProvider
-dontwarn org.bouncycastle.tsp.TimeStampRequest
-dontwarn org.bouncycastle.tsp.TimeStampRequestGenerator
-dontwarn org.bouncycastle.tsp.TimeStampResponse
-dontwarn org.bouncycastle.tsp.TimeStampToken
-dontwarn org.bouncycastle.tsp.TimeStampTokenInfo
-dontwarn org.bouncycastle.util.Selector
-dontwarn org.bouncycastle.util.Store
-dontwarn org.ietf.jgss.GSSException
-dontwarn org.ietf.jgss.Oid
-dontwarn org.w3c.dom.events.Event
-dontwarn org.w3c.dom.events.EventListener
-dontwarn org.w3c.dom.events.EventTarget
-dontwarn org.w3c.dom.events.MutationEvent
-dontwarn org.w3c.dom.svg.SVGDocument
-dontwarn org.w3c.dom.svg.SVGSVGElement
-dontwarn org.w3c.dom.traversal.DocumentTraversal
-dontwarn org.w3c.dom.traversal.NodeFilter
-dontwarn org.w3c.dom.traversal.NodeIterator

-keepattributes Signature
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.TypeAdapter