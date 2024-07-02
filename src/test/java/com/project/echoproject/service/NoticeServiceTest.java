package com.project.echoproject.service;

import com.project.echoproject.entity.Notice;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.NoticeRepository;
import com.project.echoproject.repository.SiteUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;

@SpringBootTest
public class NoticeServiceTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Test
    @Commit
    public void testCreateNotice() throws Exception {


        // 관리자로 작성해야해서 admin1234 가져오기
        SiteUser user = siteUserRepository.findByUserId("admin1234")
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 공지사항 테스트 게시글 create
        Notice noticeSample = new Notice();
        noticeSample.setNotice_title("저탄소 녹색 Life 웹사이트 소개글");
        noticeSample.setNotice_content("안녕하세요! \n" +
                "저탄소 녹색 Life 웹사이트에 방문해 주셔서 감사합니다. \n" +
                "저희 사이트는 일상속에서 저탄소 생활을 실천하는 데 필요한 다양한 정보를 제공하고 있으며, 회원 여러분의 챌린지 참여를 기다리고 있습니다. \n" +
                "\n" +
                "아래는 사이트 이용 가이드입니다.\n" +
                "\n" +
                "1. 회원 가입 및 로그인:\n" +
                "   - 사이트 상단의 '회원가입' 버튼을 클릭하여 간단한 정보를 입력하고 회원으로 가입하세요.\n" +
                "   - 이미 가입된 회원은 '로그인' 버튼을 클릭하여 로그인할 수 있습니다.\n" +
                "\n" +
                "2. 그린라이프 인증:\n" +
                "   - 그린라이프 인증은 쓰레기를 재활용하여 멋지게 활용한 모습을 인증하고 자랑하는 게시판입니다. 마음껏 자랑하여 좋아요를 받아보세요!\n" +
                "   - 일상에서 쉽게 실천할 수 있는 방법들을 확인하고 함께 실천해 보세요.\n" +
                "\n" +
                "3. 저탄소 실천 챌린지 참여:\n" +
                "   - 전자 영수증, 다회용기 사용, 텀블러 사용, 전자 메일함 비우기와 같은 저탄소 실천 챌린지 활동을 통해 활동을 인증해보세요! \n" +
                "     환경을 보호하며 포인트도 모을 수 있으며 모아놓은 포인트를 통해 에코 쇼핑mall 에서 에코제품을 구매하실 수 있습니다.\n" +
                "\n" +
                "4. 환경알리미:\n" +
                "   - 에코뉴스 모아보기 - '저탄소' 실시간 뉴스 Top 10\n" +
                "     저탄소 관련 이슈와 정책, 기술 발전, 기업의 노력을 다루는 최신 소식을 포함합니다!\n" +
                "     실시간으로 업데이트 되는 환경에 관한 최신 뉴스를 확인해보세요.\n" +
                "   - 재활용 도우미\n" +
                "     이미지 분석을 통해 올바른 재활용을 방법을 알고 재활용을 실천할 수 있습니다.\n" +
                "     재활용 방법을 얻고 싶을 때 언제든 이미지를 업로드하여 재활용에 대한 가이드를 얻어보세요.\n" +
                "\n" +
                "5. 에코 쇼핑 Mall:\n" +
                "   - 저탄소 녹색 Life 웹사이트 에서 저탄소 실천 챌린지 등을 통해 얻은 포인트로 에코 제품을 구매해보세요!\n" +
                "\n" +
                "6. 공지사항 확인:\n" +
                "   - 공지사항 페이지에서는 저희 사이트의 최신 소식과 업데이트 정보를 확인할 수 있습니다.\n" +
                "   - 중요한 정보와 행사 안내 등을 빠르게 확인해 보세요.\n" +
                "\n" +
                "저희 저탄소 녹색 Life 웹사이트는 여러분의 적극적인 참여로 더욱 의미 있는 공간이 됩니다!\n" +
                "많은 관심과 참여 부탁드립니다. 감사합니다.\n" +
                "\n" +
                "\n");
        noticeSample.setCreateDate(LocalDateTime.now());
        noticeSample.setSiteUser(user);
        noticeRepository.save(noticeSample);

    }


}
