import { NextResponse } from "next/server";

export async function middleware(req) {
  const token = req.cookies.get("token")?.value;
  const pathname = req.nextUrl.pathname;

  if (!token && pathname.match(/^\/[^/]+\/admin(\/.*)?$/)) {
    return NextResponse.redirect(new URL("/login", req.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/:slug/admin", "/:slug/admin/:path*"],
};
