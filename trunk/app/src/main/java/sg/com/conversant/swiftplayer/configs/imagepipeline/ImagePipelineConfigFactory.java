/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package sg.com.conversant.swiftplayer.configs.imagepipeline;


import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.util.HashSet;
import java.util.Set;

import sg.com.conversant.swiftplayer.configs.ConfigConstants;

/**
 * Creates ImagePipeline configuration for the sample app
 */
public class ImagePipelineConfigFactory {
  private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";

  private static ImagePipelineConfig sImagePipelineConfig;
  private static ImagePipelineConfig sOkHttpImagePipelineConfig;

  /**
   * Creates config using android http stack as network backend.
   */
  public static ImagePipelineConfig getImagePipelineConfig(Context context) {
    if (sImagePipelineConfig == null) {
      ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context);
      configureCaches(configBuilder, context);
      configureLoggingListeners(configBuilder);
      configureOptions(configBuilder);
      sImagePipelineConfig = configBuilder.build();
    }
    return sImagePipelineConfig;
  }

  /**
   * Configures disk and memory cache not to exceed common limits
   */
  private static void configureCaches(
      ImagePipelineConfig.Builder configBuilder,
      Context context) {
    final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
        ConfigConstants.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
        Integer.MAX_VALUE,                     // Max entries in the cache
        ConfigConstants.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
        Integer.MAX_VALUE,                     // Max length of eviction queue
        Integer.MAX_VALUE);                    // Max cache entry size
    configBuilder
        .setBitmapMemoryCacheParamsSupplier(
            new Supplier<MemoryCacheParams>() {
              public MemoryCacheParams get() {
                return bitmapCacheParams;
              }
            })
        .setMainDiskCacheConfig(
            DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.getApplicationContext().getExternalCacheDir())
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                .setMaxCacheSize(ConfigConstants.MAX_DISK_CACHE_SIZE)
                .build());
  }

  private static void configureLoggingListeners(ImagePipelineConfig.Builder configBuilder) {
    Set<RequestListener> requestListeners = new HashSet<>();
    requestListeners.add(new RequestLoggingListener());
    configBuilder.setRequestListeners(requestListeners);
  }

  private static void configureOptions(ImagePipelineConfig.Builder configBuilder) {
    configBuilder.setDownsampleEnabled(true);
  }
}
