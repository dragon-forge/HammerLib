package org.zeith.hammerlib.core.js;

import java.util.List;

public record CallerSpec(String method, List<String> args, boolean hasReturn)
{
}