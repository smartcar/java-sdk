#!/usr/bin/env sh
fail(){
  echo "ERROR: NECESSARY ENV VARIABLE(S) MISSING"
  exit 1
}

for VAR in "$SONATYPE_USER" "$SONATYPE_PASS" \
"$PGP_ID" "$PGP_PASS" "$PGP_FILE"; do
  test -z "$VAR" && fail
done

echo gradle uploadArchives \
-Pnexus{Username="$SONATYPE_USER",Password="$SONATYPE_PASS"} \
-Psigning.{keyId="$PGP_ID",password="$PGP_PASS",secretKeyRingFile="$PGP_FILE"}