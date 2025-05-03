# Misleading error when library is not found

## Reproduction

To reproduce this project must be built as an android bundle:

```bash
flutter build appbundle
```

New app should be installed as an bundle-part with bundle tool:

1. Build apk from bundle:

```bash
 java -jar bundletool-all-1.18.1.jar build-apks --local-testing --bundle=build/app/outputs/bundle/debug/app-debug.aab --output=apps.apks --overwrite
```

2. Install with bundletool:

```bash
java -jar bundletool-all-1.18.1.jar install-apks --apks=apps.apks
```

