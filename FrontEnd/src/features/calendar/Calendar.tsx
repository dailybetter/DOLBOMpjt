import { EventApi } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid';
import { useCallback, useEffect, useState } from 'react';

import { axiosService } from '../../api/instance';
import { useModalControl } from '../../hooks/useModalControl';
import { EventModal } from './EventModal';

export const Calendar = () => {
  const [events, setEvents] = useState<EventApi[]>([]);
  const [currentEvents, setCurrentEvents] = useState<EventApi[]>([]);
  const [isEditCard, setIsEditCard] = useState<boolean>(false);
  const [toggle, setToggle] = useState<boolean>(false);
  const [eventInfos, setEventInfos] = useState<any>();
  const [clickInfos, setClickInfos] = useState<any>();
  const modalControl = useModalControl(false);
  const UpdateToggle = () => {
    setToggle(!toggle);
  };
  useEffect(() => {
    axiosService
      .get('/schedule/')
      .then((res) => {
        setEvents(res.data);
      })
      .catch((err) => console.log(`초기랜더링 에러${err}`));
  }, [toggle]);
  //드롭
  const eventDrop = (info: any) => {
    const updateId = info.event.extendedProps.scheduleId;
    axiosService.get(`schedule/${updateId}`).then((res) => {
      const DropData = {
        scheduleId: updateId,
        clientId: res.data.clientId,
        title: res.data.title,
        start: new Date(info.event.startStr).toISOString(),
        end: new Date(info.event.endStr).toISOString(),
        content: res.data.content,
      };
      axiosService.put(`/schedule/`, DropData).then((res) => console.log(res));
    });
  };

  const [currentEvents, setCurrentEvents] = useState<EventApi[]>([]);
  const handleEvents = useCallback((events: EventApi[]) => setCurrentEvents(events), []);
  //날짜 선택시 이벤트 모달 오픈, 이벤트 추가
  const dateSelect = (selectInfo: any) => {
    setIsEditCard(false);
    setEventInfos(selectInfo);
    modalControl.handleOpen();
  };
  //이벤트 클릭 시
  const handleEventClick = (clickInfo: any) => {
    setIsEditCard(true);
    setEventInfos(clickInfo);
    modalControl.handleOpen();
  };
  return (
    <div className="Calendar">
      <EventModal
        open={modalControl.isOpen}
        handleClose={modalControl.handleClose}
        eventInfos={eventInfos}
        clickInfos={clickInfos}
        isEditCard={isEditCard}
        update={UpdateToggle}
      />
      <div>
        <FullCalendar
          plugins={[dayGridPlugin, interactionPlugin, timeGridPlugin]}
          headerToolbar={{
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay',
          }}
          initialView="dayGridMonth"
          selectable={true}
          editable={true}
          events={events}
          locale="kr"
          eventsSet={handleEvents}
          select={dateSelect}
          eventClick={handleEventClick}
          eventDrop={eventDrop}
        />
      </div>
    </div>
  );
};
